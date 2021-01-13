package core;

import core.boundary.*;
import core.dto.UserDTO;
import core.entity.ActivationToken;
import core.entity.User.UserRole;
import core.gateway.ActivationTokenGateway;
import core.mock.MockActivationTokenRepo;
import core.mock.MockUserRepository;
import core.mock.TestToken;
import core.usecase.UseCaseException;
import core.usecase.UseCaseException.NoSuchUserException;
import core.usecase.UseCaseException.UserAlreadyExistsException;
import core.usecase.user.*;
import core.usecase.user.CreateUser.UserRegistrationCallback;
import core.utils.ExpiringToken.TokenHasExpiredException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserInteractorTest {

    static UserIOBoundary userInteractor;
    static final ActivationTokenGateway tokenRepo = new MockActivationTokenRepo();

    @BeforeAll
    public static void setUp() {
        userInteractor = new UserInteractorManager(new MockUserRepository(), tokenRepo);
        createUsers();
    }

    public static void createUsers() {
        var names = new String[] {"john", "mary", "echidna", "julian"};
        var callsToCallback = new ArrayList<Integer>();
        for (String username : names) {
            var request = new CreateUser.RequestModel(username, username + "@gmail.com",
                    "asdf90234", token -> {
                        callsToCallback.add(1);
                        assertTrue(token instanceof ActivationToken);
                        assertDoesNotThrow(() -> UUID.fromString(token.toString()));
                    });
            var expected = new CreateUser.ResponseModel("User '" + username + "' created.");
            var actual = userInteractor.createUser(request, chars -> chars.toString());
            assertEquals(expected, actual);
        }
        assertEquals(names.length, callsToCallback.size());
    }

    @Test
    public void createUsersThrowsException() {
        UserRegistrationCallback cb = token -> {
        };
        var blankUsernameInRequest =
                new CreateUser.RequestModel("", "af@gmail.com", "asdfwetewt", cb);
        var blankEmailInRequest = new CreateUser.RequestModel("asdf", "", "asdfwetewt", cb);
        var shortPasswordInRequest =
                new CreateUser.RequestModel("asdf", "asdf@gmail.com", "asdf", cb);

        assertThrows(IllegalArgumentException.class,
                () -> userInteractor.createUser(blankUsernameInRequest, chars -> chars.toString()));
        assertThrows(IllegalArgumentException.class,
                () -> userInteractor.createUser(blankEmailInRequest, chars -> chars.toString()));
        assertThrows(IllegalArgumentException.class,
                () -> userInteractor.createUser(shortPasswordInRequest, chars -> chars.toString()));

        assertThrows(NullPointerException.class, () -> {
            var nullPassword = new CreateUser.RequestModel("asdfasdf", "asdf@gmail.com", null, cb);
            userInteractor.createUser(nullPassword, chars -> chars.toString());
        });
        assertThrows(NullPointerException.class, () -> {
            var nullUsername = new CreateUser.RequestModel(null, "asd@gmail.com", "asdfwe3244", cb);
            userInteractor.createUser(nullUsername, chars -> chars.toString());
        });
    }

    @Test
    public void createDuplicateUserThrowsException() {
        UserRegistrationCallback cb = token -> {
        };
        var request = new CreateUser.RequestModel("mary", "mary@gmail.com", "adsfsdf", cb);
        assertThrows(UserAlreadyExistsException.class,
                () -> userInteractor.createUser(request, chars -> chars.toString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"julian", "mary", "echidna"})
    public void getProfile(String username) {
        var request = new RetrieveProfile.RequestModel(username);
        var expected = new RetrieveProfile.ResponseModel(
                new UserDTO(0, username, username + "@gmail.com", UserRole.UNACTIVATED));
        var actual = userInteractor.retrieveUserProfile(request);
        assertEquals(expected.getUser(), actual.getUser());
    }

    @Test
    public void getProfileThrowsException() {
        var nullUsernameInRequest = new RetrieveProfile.RequestModel(null);
        var blankUsernameInRequest = new RetrieveProfile.RequestModel("");

        assertThrows(NoSuchUserException.class,
                () -> userInteractor.retrieveUserProfile(blankUsernameInRequest));
        assertThrows(NoSuchUserException.class,
                () -> userInteractor.retrieveUserProfile(nullUsernameInRequest));
    }

    @Test
    public void registerNewUser() {
        String username = "Estere";
        String email = username + "@gmail.com";
        StringBuilder tokenStr = new StringBuilder();

        var createRequest = new CreateUser.RequestModel(username, email, "asdflkwer",
                token -> tokenStr.append(token));
        userInteractor.createUser(createRequest, chars -> chars.toString());

        var badActivationRequest =
                new ActivateUser.RequestModel(username, "asdlfkubyweirlkaslkdur");
        assertThrows(UseCaseException.class,
                () -> userInteractor.activateUser(badActivationRequest));

        var activationRequest = new ActivateUser.RequestModel(username, tokenStr.toString());
        ActivateUser.ResponseModel activationResponse =
                userInteractor.activateUser(activationRequest);
        assertEquals("User '" + username + "' has been activated.",
                activationResponse.getMessage());

        var retrievalRequest = new RetrieveProfile.RequestModel(username);
        var retrievalResponse = userInteractor.retrieveUserProfile(retrievalRequest);
        assertEquals(UserRole.USER, retrievalResponse.getUser().getRole());

        assertFalse(tokenRepo.findByUser_username(username).isPresent());
    }

    @Test
    void registerNewUserFailsWhenTokenExpired() throws InterruptedException {
        String username = "Ester";
        String email = username + "@gmail.com";
        StringBuilder tokenStr = new StringBuilder();

        var createRequest = new CreateUser.RequestModel(username, email, "asdflkwer",
                token -> tokenStr.append(token));
        userInteractor.createUser(createRequest, chars -> chars.toString());

        long milliseconds = 5;
        tokenRepo.save(new ActivationToken(username, new TestToken(UUID.fromString(tokenStr.toString()), milliseconds)));
        Thread.sleep(milliseconds);

        var activationRequest = new ActivateUser.RequestModel(username, tokenStr.toString());
        assertThrows(TokenHasExpiredException.class,
                () -> userInteractor.activateUser(activationRequest));

        var retrievalRequest = new RetrieveProfile.RequestModel(username);
        var retrievalResponse = userInteractor.retrieveUserProfile(retrievalRequest);
        assertEquals(UserRole.UNACTIVATED, retrievalResponse.getUser().getRole());
    }

    @Test
    public void updateUser() {
        var request = new UpdateUserInfo.RequestModel("john", "josh");
        var expected = new UpdateUserInfo.ResponseModel("User info has been updated successfully.");
        var actual = userInteractor.updateUserInfo(request);
        assertEquals(expected, actual);

        var request1 = new RetrieveProfile.RequestModel("josh");
        var expected1 = new RetrieveProfile.ResponseModel(
                new UserDTO(0, "josh", "john@gmail.com", UserRole.UNACTIVATED));
        var actual1 = userInteractor.retrieveUserProfile(request1);
        assertEquals(expected1, actual1);

        var request2 = new RetrieveProfile.RequestModel("john");
        assertThrows(NoSuchUserException.class, () -> userInteractor.retrieveUserProfile(request2));
    }

    @Test
    public void updateUserWithBlankNewUsernameThrowsException() {
        var request = new UpdateUserInfo.RequestModel("alice", "");
        assertThrows(IllegalArgumentException.class, () -> userInteractor.updateUserInfo(request));
    }

    @Test
    public void updateUserWithReservedNameThrowsException() {
        var request = new UpdateUserInfo.RequestModel("julian", "mary");
        assertThrows(UserAlreadyExistsException.class,
                () -> userInteractor.updateUserInfo(request));
    }

}

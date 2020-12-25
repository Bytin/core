package core;

import core.boundary.*;
import core.dto.UserDTO;
import core.mock.MockUserRepository;
import core.usecase.UseCaseException.NoSuchUserException;
import core.usecase.UseCaseException.UserAlreadyExistsException;
import core.usecase.user.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserInteractorTest {

        static UserIOBoundary userInteractor;

        @BeforeAll
        public static void setUp() {
                userInteractor = new UserInteractorManager(new MockUserRepository());
                createUsers();
        }

        public static void createUsers() {
                var names = new String[] {"john", "mary", "echidna", "julian"};
                for (String username : names) {
                        var request = new CreateUser.RequestModel(username, "asdf90234");
                        var expected = new CreateUser.ResponseModel(
                                        "User '" + username + "' created.");
                        var actual = userInteractor.createUser(request, chars -> chars.toString());
                        assertEquals(expected, actual);
                }
        }

        @Test
        public void createUsersThrowsException() {
                var blankUsernameInRequest =
                                new CreateUser.RequestModel("", "asdfwetewt");
                var shortPasswordInRequest =
                                new CreateUser.RequestModel("asdf", "asdf");

                assertThrows(IllegalArgumentException.class, () -> userInteractor
                                .createUser(blankUsernameInRequest, chars -> chars.toString()));
                assertThrows(IllegalArgumentException.class, () -> userInteractor
                                .createUser(shortPasswordInRequest, chars -> chars.toString()));

                assertThrows(NullPointerException.class, () -> {
                        var nullPassword = new CreateUser.RequestModel("asdfasdf", null);
                        userInteractor.createUser(nullPassword, chars -> chars.toString());
                });
                assertThrows(NullPointerException.class, () -> {
                        var nullUsername = new CreateUser.RequestModel(null, "asdfwe3244");
                        userInteractor.createUser(nullUsername, chars -> chars.toString());
                });
        }

        @Test
        public void createDuplicateUserThrowsException() {
                var request = new CreateUser.RequestModel("mary", "adsfsdf");
                assertThrows(UserAlreadyExistsException.class, () -> userInteractor
                                .createUser(request, chars -> chars.toString()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"julian", "mary", "echidna"})
        public void getProfile(String username) {
                var request = new RetrieveProfile.RequestModel(username);
                var expected = new RetrieveProfile.ResponseModel(
                                new UserDTO(0, username));
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
        public void updateUser() {
                var request = new UpdateUserInfo.RequestModel("john", "josh", "@gmail.com");
                var expected = new UpdateUserInfo.ResponseModel(
                                "User info has been updated successfully.");
                var actual = userInteractor.updateUserInfo(request);
                assertEquals(expected, actual);

                var request1 = new RetrieveProfile.RequestModel("josh");
                var expected1 = new RetrieveProfile.ResponseModel(
                                new UserDTO(0, "josh"));
                var actual1 = userInteractor.retrieveUserProfile(request1);
                assertEquals(expected1, actual1);

                var request2 = new RetrieveProfile.RequestModel("john");
                assertThrows(NoSuchUserException.class,
                                () -> userInteractor.retrieveUserProfile(request2));
        }

        @Test
        public void updateUserWithReservedNameThrowsException() {
                var request = new UpdateUserInfo.RequestModel("julian", "mary", "@gmail.com");
                assertThrows(UserAlreadyExistsException.class,
                                () -> userInteractor.updateUserInfo(request));
        }

}

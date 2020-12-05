package core;

import core.boundary.*;
import core.mock.MockUserRepository;
import core.usecase.Command.IllegalRequestModelException;
import core.usecase.user.*;
import core.usecase.user.AbstractUserInteractor.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(Lifecycle.PER_CLASS)
public class UserInteractorTest {

    UserIOBoundary userInteractor;

    @BeforeAll
    public void setUp() {
        userInteractor = new UserInteractorManager(new MockUserRepository());
        createUsers();
    }

    public void createUsers() {
        var names = new String[] { "john", "mary", "echidna", "julian" };
        for (String username : names) {
            var request = new CreateUser.RequestModel(username, "asdf90234", username + "@gmail.com");
            var expected = new CreateUser.ResponseModel("User '" + username + "' created.");
            var actual = userInteractor.createUser(request);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void createUsersThrowsException() {
        var blankUsernameInRequest = new CreateUser.RequestModel("", "asdfwetewt", "@gmail.com");
        var shortPasswordInRequest = new CreateUser.RequestModel("asdf", "asdf", "@gmail.com");
        var blankEmailInRequest = new CreateUser.RequestModel("asdfasdf", "asdfwe3244", "");
        var nullPassword = new CreateUser.RequestModel("asdfasdf", null, "sadflkj");
        var nullUsername = new CreateUser.RequestModel(null, "asdfwe3244", "asdfl");

        assertThrows(IllegalRequestModelException.class, () -> userInteractor.createUser(blankUsernameInRequest));
        assertThrows(IllegalRequestModelException.class, () ->  userInteractor.createUser(shortPasswordInRequest));
        assertThrows(IllegalRequestModelException.class, () -> userInteractor.createUser(blankEmailInRequest));
        assertThrows(IllegalRequestModelException.class, () -> userInteractor.createUser(nullPassword));
        assertThrows(IllegalRequestModelException.class, () -> userInteractor.createUser(nullUsername));
    }

    @Test
    public void createDuplicateUserThrowsException(){
        var request = new CreateUser.RequestModel("mary", "adsfsdf", "slkdfj");
        assertThrows(UserAlreadyExistsException.class, () -> userInteractor.createUser(request));
    }

    @ParameterizedTest
    @CsvSource(value = {"4, julian", "2, mary", "3, echidna"})
    public void getProfile( long id, String username) {
        var request = new RetrieveProfile.RequestModel(username);
        var expected = new RetrieveProfile.ResponseModel(id, username, username + "@gmail.com");
        var actual = userInteractor.retrieveUserProfile(request);
        assertEquals(expected, actual);
    }

    @Test
    public void getProfileThrowsException() {
        var nullUsernameInRequest = new RetrieveProfile.RequestModel(null);
        var blankUsernameInRequest = new RetrieveProfile.RequestModel("");

        assertThrows(IllegalRequestModelException.class, () -> userInteractor.retrieveUserProfile(blankUsernameInRequest));
        assertThrows(IllegalRequestModelException.class, () -> userInteractor.retrieveUserProfile(nullUsernameInRequest));
    }

    @Test
    public void updateUser(){
        var request = new UpdateUserInfo.RequestModel("john", "josh", "@gmail.com");
        var expected = new UpdateUserInfo.ResponseModel("User info has been updated successfully.");
        var actual = userInteractor.updateUserInfo(request);
        assertEquals(expected, actual);

        var request1 = new RetrieveProfile.RequestModel("josh");
        var expected1 = new RetrieveProfile.ResponseModel(1, "josh", "@gmail.com");
        var actual1 = userInteractor.retrieveUserProfile(request1);
        assertEquals(expected1, actual1);

        var request2 = new RetrieveProfile.RequestModel("john");
        assertThrows(NoSuchUserException.class, () -> userInteractor.retrieveUserProfile(request2));
    }

    @Test
    public void updateUserWithReservedNameThrowsException(){
        var request = new UpdateUserInfo.RequestModel("julian", "mary", "@gmail.com");
        assertThrows(UserAlreadyExistsException.class, () -> userInteractor.updateUserInfo(request));
    }


}

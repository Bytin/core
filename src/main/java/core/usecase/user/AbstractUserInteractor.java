package core.usecase.user;

import core.gateway.UserGateway;

public abstract class AbstractUserInteractor {

        UserGateway gateway;

        AbstractUserInteractor(UserGateway gateway) {
                this.gateway = gateway;
        }

}

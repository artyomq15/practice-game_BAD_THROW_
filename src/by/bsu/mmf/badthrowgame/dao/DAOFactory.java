package by.bsu.mmf.badthrowgame.dao;


import by.bsu.mmf.badthrowgame.dao.impl.ServerDAOImpl;

public class DAOFactory {
    private static final DAOFactory instance = new DAOFactory();

    private final ServerDAO serverDAO = new ServerDAOImpl();

    private DAOFactory() {}

    public ServerDAO getServerDAO() {
        return serverDAO;
    }

    public static DAOFactory getInstance() {
        return instance;
    }
}

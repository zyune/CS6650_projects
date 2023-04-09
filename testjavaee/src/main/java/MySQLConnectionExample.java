import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;

public class MySQLConnectionExample {
    public static void main(String[] args)
            throws NotSupportedException, IllegalStateException, RollbackException, ClassNotFoundException {
        String url1 = "jdbc:mysql://localhost:3306/db1";
        String url2 = "jdbc:mysql://localhost:3306/db2";
        String user = "root";
        String password = "The1isyou";
        String insertSQL = "INSERT INTO clients (client_id) VALUES (abcdef) ";

        try {
            // Create AtomikosDataSourceBean for each database
            MysqlXADataSource xaDataSource1 = new MysqlXADataSource();
            xaDataSource1.setUrl(url1);
            xaDataSource1.setUser(user);
            xaDataSource1.setPassword(password);
            AtomikosDataSourceBean ds1 = new AtomikosDataSourceBean();
            ds1.setXaDataSource(xaDataSource1);
            ds1.setUniqueResourceName("db1");

            MysqlXADataSource xaDataSource2 = new MysqlXADataSource();
            xaDataSource2.setUrl(url2);
            xaDataSource2.setUser(user);
            xaDataSource2.setPassword(password);
            AtomikosDataSourceBean ds2 = new AtomikosDataSourceBean();
            ds2.setXaDataSource(xaDataSource2);
            ds2.setUniqueResourceName("db2");

            UserTransaction userTransaction = new UserTransactionImp();
            TransactionManager transactionManager = new UserTransactionManager();

            // Begin the transaction
            userTransaction.begin();

            Transaction transaction = transactionManager.getTransaction();
            Connection connection1 = ds1.getConnection();
            Connection connection2 = ds2.getConnection();

            // Enlist the resources
            transaction.enlistResource(connection1.unwrap(javax.sql.XAConnection.class).getXAResource());
            transaction.enlistResource(connection2.unwrap(javax.sql.XAConnection.class).getXAResource());

            try (PreparedStatement pstmt1 = connection1.prepareStatement(insertSQL);
                    PreparedStatement pstmt2 = connection2.prepareStatement(insertSQL)) {

                // Perform database operations for the first connection
                pstmt1.setString(1, "Hello, World!");
                pstmt1.executeUpdate();

                // Perform database operations for the second connection
                pstmt2.setString(1, "Hello, World!");
                pstmt2.executeUpdate();

                // Commit the transaction
                userTransaction.commit();

                System.out.println("Transaction committed successfully.");
            } catch (Exception e) {
                System.err.println("Error during transaction. Rolling back.");
                userTransaction.rollback();
                e.printStackTrace();
            } finally {
                connection1.close();
                connection2.close();
            }
        } catch (SQLException | SystemException e) {
            System.err.println("Error connecting to the MySQL database!");
            e.printStackTrace();
        }
    }
}
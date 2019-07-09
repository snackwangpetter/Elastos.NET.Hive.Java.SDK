package org.elastos.hive.OneDrive;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;

import org.elastos.hive.Client;
import org.elastos.hive.Drive;
import org.elastos.hive.DriveType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class OneDriveClientTest {
	private static Client client;
	@Test public void testGetInstance() {
		assertNotNull(Client.getInstance(DriveType.oneDrive));
	}

	@Test public void testGetInfo() {
		try {
			Client.Info info = client.getInfo().get();
			assertNotNull(info);
			assertNotNull(info.get(Client.Info.userId));
			assertNotNull(info.get(Client.Info.name));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("getInfo failed");
		}
	}

	@Test public void testGetDefaultDrive() {
		try {
			Drive drive = client.getDefaultDrive().get();
			assertNotNull(drive);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("getInfo failed");
		}
	}

	@BeforeClass
	static public void setUp() throws Exception {
		client = OneDriveTestBase.login();
		assertNotNull(client);
	}

    @AfterClass
    static public void tearDown() throws Exception {
    	client.logout();
    }
}
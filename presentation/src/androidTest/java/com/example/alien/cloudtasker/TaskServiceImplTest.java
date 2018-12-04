package com.example.alien.cloudtasker;


import android.content.Context;

import com.example.data.database.ITaskDao;
import com.example.data.database.TaskDatabase;
import com.example.data.model.DatabaseUser;
import com.example.data.repository.TaskLocalRepository;
import com.example.data.repository.TaskRemoteRepository;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;
import com.example.domain.service.ITaskService;
import com.example.domain.service.TaskServiceImpl;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {
    private TaskDatabase mTaskDatabase;
    private ITaskDao mTaskDao;
    private String mUpdatedUserId = "sdasdDSda3Adcr3edEwsffr";
    private String mUpdatedUserName = "NewUserName";

    private ITaskRepository mTaskLocalRepository;

    @Mock
    private TaskRemoteRepository mTaskRemoteRepositoryMockSingleUserWithBadUser;

    @Mock
    private TaskRemoteRepository mTaskRemoteRepositoryMockAddAndDeleteUser;

    @Mock
    private TaskRemoteRepository mTaskRemoteRepositoryMockUpdateUser;

    @Mock
    private TaskRemoteRepository mTaskRemoteRepositoryWithPublisher;

    private PublishProcessor<DomainUser> mDomainUserPublishProcessor;

    private ITaskService mTaskService;

    @Rule
    public TrampolineSchedulerRule mTrampolineSchedulerRule = new TrampolineSchedulerRule();

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        mTaskDatabase = Room.inMemoryDatabaseBuilder(context, TaskDatabase.class).build();
        mTaskDao = mTaskDatabase.getTaskDao();
        mTaskLocalRepository = new TaskLocalRepository(mTaskDao);
        mDomainUserPublishProcessor = PublishProcessor.create();
        initTaskRemoteRepositoryMockSingleUserWithBadUser();
        initTaskRemoteRepositoryMockAddAndDeleteUser();
        initTaskRemoteRepositoryMockUpdateUser();
    }

    private void initTaskRemoteRepositoryMockUpdateUser() {
        List<DomainUser> domainUsers = new ArrayList<>();
        DomainUser addedDomainUser = new DomainUser(mUpdatedUserId, "user01", DomainUser.Type.ADDED);
        addedDomainUser.setLastLoginTime(new Date());
        DomainUser updatedDomainUser = new DomainUser(mUpdatedUserId, mUpdatedUserName, DomainUser.Type.MODIFIED);
        updatedDomainUser.setLastLoginTime(new Date());
        domainUsers.add(addedDomainUser);
        domainUsers.add(updatedDomainUser);
        when(mTaskRemoteRepositoryMockUpdateUser.getUserList()).thenReturn(Flowable.just(domainUsers));
    }

    private void initTaskRemoteRepositoryMockSingleUserWithBadUser() {
        List<DomainUser> domainUsers = new ArrayList<>();
        DomainUser domainUser = new DomainUser("ewrwrsfwewWer32wewe", "user01", DomainUser.Type.ADDED);
        domainUser.setLastLoginTime(new Date());
        domainUsers.add(domainUser);
        domainUsers.add(new DomainUser("asdaasd", "broken user", DomainUser.Type.ADDED));
        when(mTaskRemoteRepositoryMockSingleUserWithBadUser.getUserList()).thenReturn(Flowable.just(domainUsers));
    }

    private void initTaskRemoteRepositoryMockAddAndDeleteUser() {
        List<DomainUser> domainUsers = new ArrayList<>();
        DomainUser addedDomainUser = new DomainUser("ewrwrsfwewWer32wewe", "user01", DomainUser.Type.ADDED);
        addedDomainUser.setLastLoginTime(new Date());
        DomainUser removedDomainUser = new DomainUser("ewrwrsfwewWer32wewe", "user01", DomainUser.Type.REMOVED);
        removedDomainUser.setLastLoginTime(new Date());
        domainUsers.add(addedDomainUser);
        domainUsers.add(removedDomainUser);
        when(mTaskRemoteRepositoryMockAddAndDeleteUser.getUserList()).thenReturn(Flowable.just(domainUsers));
    }

    @After
    public void tearDown() throws Exception {
        mTaskService = null;
        mTaskLocalRepository = null;
        mTaskRemoteRepositoryMockSingleUserWithBadUser = null;
        mTaskDatabase.close();
    }

    @Test
    public void testTaskServiceImplCreationSingleUserWithBadUser() {
        mTaskService = new TaskServiceImpl(mTaskLocalRepository, mTaskRemoteRepositoryMockSingleUserWithBadUser);
        Single.fromCallable((Callable<Object>) () -> {
                    List<DatabaseUser> databaseUsers = mTaskDao.getUsers().blockingGet();
                    assertEquals(1, databaseUsers.size());
                    return 1;
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Test
    public void testTaskServiceImplCreationAddAndRemoveUser() {
        mTaskService = new TaskServiceImpl(mTaskLocalRepository, mTaskRemoteRepositoryMockAddAndDeleteUser);
        Single.fromCallable((Callable<Object>) () -> {
                    List<DatabaseUser> databaseUsers = mTaskDao.getUsers().blockingGet();
                    assertEquals(0, databaseUsers.size());
                    return 1;
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
    @Test
    public void testTaskServiceImplCreationUpdateUser() {
        mTaskService = new TaskServiceImpl(mTaskLocalRepository, mTaskRemoteRepositoryMockUpdateUser);
        Single.fromCallable((Callable<Object>) () -> {
                    DatabaseUser databaseUser = mTaskDao.getUserById(mUpdatedUserId).blockingGet();
                    assertEquals(mUpdatedUserId, databaseUser.getUserId());
                    assertEquals(mUpdatedUserName, databaseUser.getUserName());
                    return 1;
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public class TrampolineSchedulerRule implements TestRule {
        @Override
        public Statement apply(final Statement base, Description d) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    RxJavaPlugins.setIoSchedulerHandler(
                            scheduler -> Schedulers.trampoline());
                    RxJavaPlugins.setComputationSchedulerHandler(
                            scheduler -> Schedulers.trampoline());
                    RxJavaPlugins.setNewThreadSchedulerHandler(
                            scheduler -> Schedulers.trampoline());
                    RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                            scheduler -> Schedulers.trampoline());
                    try {
                        base.evaluate();
                    } finally {
                        RxJavaPlugins.reset();
                        RxAndroidPlugins.reset();
                    }
                }
            };
        }
    }
}
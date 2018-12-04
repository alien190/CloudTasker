package com.example.alien.cloudtasker;


import android.content.Context;

import com.example.data.database.ITaskDao;
import com.example.data.database.TaskDatabase;
import com.example.data.model.DatabaseUser;
import com.example.data.repository.TaskLocalRepository;
import com.example.data.repository.TaskRemoteRepository;
import com.example.domain.model.DomainTask;
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
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {
    private TaskDatabase mTaskDatabase;
    private ITaskDao mTaskDao;

    private ITaskRepository mTaskLocalRepository;

    @Mock
    private TaskRemoteRepository mTaskRemoteRepositoryWithPublisher;

    private PublishProcessor<List<DomainUser>> mDomainUserPublishProcessor;

    private PublishProcessor<List<DomainTask>> mDomainTaskPublishProcessor;

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
        initTaskRemoteRepositoryWithPublisher();
        mTaskService = new TaskServiceImpl(mTaskLocalRepository, mTaskRemoteRepositoryWithPublisher);
    }

    private void initTaskRemoteRepositoryWithPublisher() {
        when(mTaskRemoteRepositoryWithPublisher.getUserList()).thenReturn(mDomainUserPublishProcessor);
        when(mTaskRemoteRepositoryWithPublisher.updateUser(any())).thenAnswer((Answer<Completable>) invocation -> {
            Object[] args = invocation.getArguments();
            DomainUser domainUser = (DomainUser) args[0];
            domainUser.setType(DomainUser.Type.MODIFIED);
            domainUser.setLastLoginTime(new Date());
            List<DomainUser> domainUsers = new ArrayList<>();
            domainUsers.add(domainUser);
            mDomainUserPublishProcessor.onNext(domainUsers);
            return Completable.complete();
        });
        when(mTaskRemoteRepositoryWithPublisher.getTaskList()).thenReturn(mDomainTaskPublishProcessor);
    }

    @After
    public void tearDown() throws Exception {
        mTaskService = null;
        mTaskLocalRepository = null;
        mDomainUserPublishProcessor = null;
        mTaskRemoteRepositoryWithPublisher = null;
        mTaskDatabase.close();
    }

    @Test
    public void testTaskServiceImplCreationSingleUserWithBadUser() {
        pushFirebaseUser("adassadadaasdads", "user01", DomainUser.Type.ADDED);

        List<DomainUser> domainUsers = new ArrayList<>();
        domainUsers.add(new DomainUser("asdaasd", "broken user", DomainUser.Type.ADDED));
        mDomainUserPublishProcessor.onNext(domainUsers);

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
        String userId = "ewrwrsfwewWer32wewe";
        String displayName = "user01";

        pushFirebaseUser(userId, displayName, DomainUser.Type.ADDED);
        pushFirebaseUser(userId, displayName, DomainUser.Type.REMOVED);

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
        String updatedUserId = "sdasdDSda3Adcr3edEwsffr";
        String updatedUserName = "NewUserName";

        pushFirebaseUser(updatedUserId, "user01", DomainUser.Type.ADDED);
        pushFirebaseUser(updatedUserId, updatedUserName, DomainUser.Type.MODIFIED);

        Single.fromCallable((Callable<Object>) () -> {
                    DatabaseUser databaseUser = mTaskDao.getUserById(updatedUserId).blockingGet();
                    assertEquals(updatedUserId, databaseUser.getUserId());
                    assertEquals(updatedUserName, databaseUser.getUserName());
                    return 1;
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Test
    public void testUserUpdate() {
        String updatedUserId = "elkdfk4389jvdsfd43rksdfsd0923i0kd";
        String updatedUserName = "NewUserName";

        pushFirebaseUser(updatedUserId, "user01sdfsfs", DomainUser.Type.ADDED);
        DomainUser renamedUsers = new DomainUser(updatedUserId, updatedUserName, null);
        mTaskService.updateUser(renamedUsers);

        Single.fromCallable((Callable<Object>) () -> {
                    DatabaseUser databaseUser = mTaskDao.getUserById(updatedUserId).blockingGet();
                    assertEquals(updatedUserId, databaseUser.getUserId());
                    assertEquals(updatedUserName, databaseUser.getUserName());
                    return 1;
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Test
    public void testAddNewTask(){
        String userId = "sdfmdsfns934kflksdSdfgerSDSfdswwe3";
        mTaskDao.deleteAllUsers();
        pushFirebaseUser(userId, "userForTask", DomainUser.Type.ADDED);
    }

    private void pushFirebaseUser(String updatedUserId, String userName, DomainUser.Type type) {
        List<DomainUser> domainUsers = new ArrayList<>();
        DomainUser addedDomainUser = new DomainUser(updatedUserId, userName, type);
        addedDomainUser.setLastLoginTime(new Date());
        domainUsers.add(addedDomainUser);
        mDomainUserPublishProcessor.onNext(domainUsers);
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
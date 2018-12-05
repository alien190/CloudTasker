package com.example.alien.cloudtasker;


import android.content.Context;

import com.example.data.database.ITaskDao;
import com.example.data.database.TaskDatabase;
import com.example.data.model.DatabaseTask;
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

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import io.reactivex.Completable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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

    private static final String mUserId = "asddjSDwesdSDSDw23ws";

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        mTaskDatabase = Room.inMemoryDatabaseBuilder(context, TaskDatabase.class).build();
        mTaskDao = mTaskDatabase.getTaskDao();
        mTaskLocalRepository = new TaskLocalRepository(mTaskDao, mUserId);
        initPublishProcessors();
        initTaskRemoteRepositoryWithPublisher();
        mTaskService = new TaskServiceImpl(mTaskLocalRepository, mTaskRemoteRepositoryWithPublisher);
    }

    private void initPublishProcessors() {
        mDomainUserPublishProcessor = PublishProcessor.create();
        mDomainTaskPublishProcessor = PublishProcessor.create();
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
        when(mTaskRemoteRepositoryWithPublisher.updateTask(any())).thenAnswer((Answer<Completable>) invocation -> {
            Object[] args = invocation.getArguments();
            DomainTask domainTask = (DomainTask) args[0];
            domainTask.setType(DomainTask.Type.MODIFIED);
            List<DomainTask> domainTasks = new ArrayList<>();
            domainTasks.add(domainTask);
            mDomainTaskPublishProcessor.onNext(domainTasks);
            return Completable.complete();
        });
    }

    @After
    public void tearDown() throws Exception {
        mTaskService = null;
        mTaskLocalRepository = null;
        mDomainUserPublishProcessor = null;
        mTaskRemoteRepositoryWithPublisher = null;
        mDomainTaskPublishProcessor = null;
        mTaskDatabase.close();
    }

    @Test
    public void testTaskServiceImplCreationSingleUserWithBadUser() {
        pushFirebaseUser("adassadadaasdads", "user01", DomainUser.Type.ADDED);

        List<DomainUser> domainUsers = new ArrayList<>();
        domainUsers.add(new DomainUser("asdaasd", "broken user", DomainUser.Type.ADDED));
        mDomainUserPublishProcessor.onNext(domainUsers);

        mTaskDao.getUsers().observeOn(Schedulers.io())
                .subscribe(databaseUsers -> assertEquals(1, databaseUsers.size()));
    }

    @Test
    public void testTaskServiceImplCreationAddAndRemoveUser() {
        String userId = "ewrwrsfwewWer32wewe";
        String displayName = "user01";

        pushFirebaseUser(userId, displayName, DomainUser.Type.ADDED);
        pushFirebaseUser(userId, displayName, DomainUser.Type.REMOVED);

        mTaskDao.getUsers().observeOn(Schedulers.io())
                .subscribe(databaseUsers -> assertEquals(0, databaseUsers.size()));

    }

    @Test
    public void testTaskServiceImplCreationUpdateUser() {
        String updatedUserId = "sdasdDSda3Adcr3edEwsffr";
        String updatedUserName = "NewUserName";

        pushFirebaseUser(updatedUserId, "user01", DomainUser.Type.ADDED);
        pushFirebaseUser(updatedUserId, updatedUserName, DomainUser.Type.MODIFIED);

        testUserInDbById(updatedUserId, updatedUserName);

    }

    @Test
    public void testUserUpdate() {
        String updatedUserId = "elkdfk4389jvdsfd43rksdfsd0923i0kd";
        String updatedUserName = "NewUserName";

        pushFirebaseUser(updatedUserId, "user01sdfsfs", DomainUser.Type.ADDED);
        DomainUser renamedUsers = new DomainUser(updatedUserId, updatedUserName, null);
        mTaskService.updateUser(renamedUsers);

        testUserInDbById(updatedUserId, updatedUserName);
    }

    private void testUserInDbById(String updatedUserId, String updatedUserName) {
        mTaskDao.getUserById(updatedUserId).observeOn(Schedulers.io())
                .subscribe(databaseUser -> {
                    assertEquals(updatedUserId, databaseUser.getUserId());
                    assertEquals(updatedUserName, databaseUser.getUserName());
                });
    }

    @Test
    public void testUserListLive() {
        String userId = "ewrwrsfwewWer32wewe";
        String displayName = "user01";
        String newDisplayName = "user010101";

        int count[] = new int[1];
        count[0] = -1;

        mTaskService.getUserList()
                .observeOn(Schedulers.io())
                .subscribe(users -> {
                    switch (count[0]) {
                        case 0: {
                            assertEquals(1, users.size());
                            break;
                        }
                        case 1: {
                            assertEquals(0, users.size());
                            break;
                        }
                        case 2: {
                            assertEquals(1, users.size());
                            break;
                        }
                        case 3: {
                            assertEquals(1, users.size());
                            assertEquals(newDisplayName, users.get(0).getDisplayName());
                            break;
                        }
                    }
                    Timber.d("testUserListLive, count: %d", count[0]);
                });

        pushFirebaseUser(userId, displayName, DomainUser.Type.ADDED);
        count[0]++;
        sleepOneSecond();

        pushFirebaseUser(userId, displayName, DomainUser.Type.REMOVED);
        count[0]++;
        sleepOneSecond();

        pushFirebaseUser(userId, displayName, DomainUser.Type.ADDED);
        count[0]++;
        sleepOneSecond();

        pushFirebaseUser(userId, newDisplayName, DomainUser.Type.MODIFIED);
        count[0]++;
        sleepOneSecond();
    }

    @Test
    public void testAddNewTask() {
        String authorUserId = mUserId;
        String executorUserId = "fldsk304rwwesfd4wr23edea";
        String taskId = "taskId";
        String taskTitle = "Task title";
        String taskText = "Task text";
        String authorName = "authorUser";
        String executorName = "executorUser";

        pushFirebaseUser(authorUserId, authorName, DomainUser.Type.ADDED);
        pushFirebaseUser(executorUserId, executorName, DomainUser.Type.ADDED);

        testUserInDbById(authorUserId, authorName);
        testUserInDbById(executorUserId, executorName);

        pushFirebaseTask(taskId, authorUserId, executorUserId, taskTitle, taskText, DomainTask.Type.ADDED);
        pushFirebaseTask(taskId, authorUserId + "fff", executorUserId, taskTitle, taskText, DomainTask.Type.ADDED);

        mTaskDao.getTasksLive().observeOn(Schedulers.io())
                .subscribe(databaseTasks -> assertEquals(1, databaseTasks.size()));

        mTaskService.getTaskList().observeOn(Schedulers.io())
                .subscribe(databaseTasks -> {
                    assertEquals(1, databaseTasks.size());
                    assertEquals(authorName, databaseTasks.get(0).getAuthorName());
                    assertEquals(executorName, databaseTasks.get(0).getExecutorName());
                    assertEquals(authorUserId, databaseTasks.get(0).getAuthorId());
                    assertEquals(executorUserId, databaseTasks.get(0).getExecutorId());
                });

        sleepOneSecond();
    }

    @Test
    public void testTaskCascadeDeleteAuthor() {
        String authorUserId = "sdfmdsfns934kflksdSdfgerSDSfdswwe3";
        String executorUserId = "fldsk304rwwesfd4wr23edea";
        String taskId = "taskId";
        String taskTitle = "Task title";
        String taskText = "Task text";
        String authorName = "authorUser";
        String executorName = "executorUser";

        pushFirebaseUser(authorUserId, authorName, DomainUser.Type.ADDED);
        pushFirebaseUser(executorUserId, executorName, DomainUser.Type.ADDED);

        testUserInDbById(authorUserId, authorName);
        testUserInDbById(executorUserId, executorName);

        pushFirebaseTask(taskId, authorUserId, executorUserId, taskTitle, taskText, DomainTask.Type.ADDED);

        pushFirebaseUser(authorUserId, authorName, DomainUser.Type.REMOVED);

        mTaskService.getTaskList().observeOn(Schedulers.io())
                .subscribe(databaseTasks -> assertEquals(0, databaseTasks.size()));

        sleepOneSecond();
    }

    @Test
    public void testTaskCascadeDeleteExecutor() {
        String authorUserId = "sdfmdsfns934kflksdSdfgerSDSfdswwe3";
        String executorUserId = "fldsk304rwwesfd4wr23edea";
        String taskId = "taskId";
        String taskTitle = "Task title";
        String taskText = "Task text";
        String authorName = "authorUser";
        String executorName = "executorUser";

        pushFirebaseUser(authorUserId, authorName, DomainUser.Type.ADDED);
        pushFirebaseUser(executorUserId, executorName, DomainUser.Type.ADDED);

        testUserInDbById(authorUserId, authorName);
        testUserInDbById(executorUserId, executorName);

        pushFirebaseTask(taskId, authorUserId, executorUserId, taskTitle, taskText, DomainTask.Type.ADDED);

        pushFirebaseUser(executorUserId, executorName, DomainUser.Type.REMOVED);

        mTaskService.getTaskList().observeOn(Schedulers.io())
                .subscribe(databaseTasks -> assertEquals(0, databaseTasks.size()));

        sleepOneSecond();
    }


    @Test
    public void testAddNewTaskDb() {
        String authorUserId = "a_userId";
        String executorUserId = "e_userId";
        String taskId = "taskId";
        String taskTitle = "Task title";
        String taskText = "Task text";

        DatabaseTask databaseTask = new DatabaseTask();
        databaseTask.setTaskId(taskId);
        databaseTask.setTaskId(taskId);
        databaseTask.setAuthorId(authorUserId);
        databaseTask.setExecutorId(executorUserId);
        databaseTask.setTitle(taskTitle);
        databaseTask.setText(taskText);

        pushFirebaseUser(authorUserId, "user01", DomainUser.Type.ADDED);
        pushFirebaseUser(executorUserId, "user02", DomainUser.Type.ADDED);

        mTaskDao.insertTask(databaseTask);

        mTaskDao.getTasksLive().observeOn(Schedulers.io())
                .subscribe(databaseTasks -> assertEquals(1, databaseTasks.size()));

        sleepOneSecond();
    }

    @Test
    public void testRemoveAndUpdateTask() {
        String authorUserId = "sdfmdsfns934kflksdSdfgerSDSfdswwe3";
        String executorUserId = "fldsk304rwwesfd4wr23edea";
        String taskOneId = "taskOneId";
        String taskTwoId = "taskTwoId";
        String taskTitle = "Task title";
        String taskText = "Task text";
        String authorName = "authorUser";
        String executorName = "executorUser";
        String newTaskTitle = "Task title";
        String newTaskText = "Task text";

        int count[] = new int[1];
        count[0] = 0;

        pushFirebaseUser(authorUserId, authorName, DomainUser.Type.ADDED);
        pushFirebaseUser(executorUserId, executorName, DomainUser.Type.ADDED);

        testUserInDbById(authorUserId, authorName);
        testUserInDbById(executorUserId, executorName);


        mTaskDao.getTasksLive().observeOn(Schedulers.io())
                .subscribe(databaseTasks -> {
                    switch (count[0]) {
                        case 0: {
                            assertEquals(1, databaseTasks.size());
                            break;
                        }
                        case 1: {
                            assertEquals(2, databaseTasks.size());
                            break;
                        }
                        case 2: {
                            assertEquals(1, databaseTasks.size());
                            assertEquals(taskTwoId, databaseTasks.get(0).getTaskId());
                            break;
                        }
                        case 3: {
                            assertEquals(1, databaseTasks.size());
                            assertEquals(taskTwoId, databaseTasks.get(0).getTaskId());
                            assertEquals(newTaskTitle, databaseTasks.get(0).getTitle());
                            assertEquals(newTaskText, databaseTasks.get(0).getText());
                            break;
                        }
                    }
                    Timber.d("testRemoveTask, count: %d", count[0]);
                });

        pushFirebaseTask(taskOneId, authorUserId, executorUserId, taskTitle, taskText, DomainTask.Type.ADDED);
        sleepOneSecond();

        count[0]++;
        pushFirebaseTask(taskTwoId, authorUserId, executorUserId, taskTitle, taskText, DomainTask.Type.ADDED);
        sleepOneSecond();

        count[0]++;
        pushFirebaseTask(taskOneId, authorUserId, executorUserId, taskTitle, taskText, DomainTask.Type.REMOVED);
        sleepOneSecond();

        count[0]++;
        DomainTask domainTask = new DomainTask();
        domainTask.setTaskId(taskTwoId);
        domainTask.setAuthorId(authorUserId);
        domainTask.setExecutorId(executorUserId);
        domainTask.setTitle(newTaskTitle);
        domainTask.setText(taskText);
        mTaskService.updateTask(domainTask);
        sleepOneSecond();
    }

    private void sleepOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void testAddNewUserDb() {
        String authorUserId = "sdfmdsfns934kflksdSdfgerSDSfdswwe3";
        String authorUserName = "user";


        DatabaseUser databaseUser = new DatabaseUser();
        databaseUser.setUserId(authorUserId);
        databaseUser.setUserName(authorUserName);
        databaseUser.setLastLoginTime(new Date().getTime());

        mTaskDao.deleteAllUsers();

        mTaskDao.insertUser(databaseUser);

        mTaskDao.getUsers().observeOn(Schedulers.io())
                .subscribe(databaseUsers -> assertEquals(1, databaseUsers.size()));

    }


    private void pushFirebaseUser(String updatedUserId, String userName, DomainUser.Type type) {
        List<DomainUser> domainUsers = new ArrayList<>();
        DomainUser addedDomainUser = new DomainUser(updatedUserId, userName, type);
        addedDomainUser.setLastLoginTime(new Date());
        domainUsers.add(addedDomainUser);
        mDomainUserPublishProcessor.onNext(domainUsers);
    }

    private void pushFirebaseTask(String taskId,
                                  String authorId,
                                  String executorId,
                                  String title,
                                  String text,
                                  DomainTask.Type type) {
        List<DomainTask> domainTasks = new ArrayList<>();
        DomainTask domainTask = new DomainTask();
        domainTask.setTaskId(taskId);
        domainTask.setAuthorId(authorId);
        domainTask.setExecutorId(executorId);
        domainTask.setTitle(title);
        domainTask.setText(text);
        domainTask.setType(type);
        domainTasks.add(domainTask);
        mDomainTaskPublishProcessor.onNext(domainTasks);
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
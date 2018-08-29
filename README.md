# MyGroceryStore


1、生成广播的方法：Context.sendBroadcast(),Context.sendOrderedBroadcast(),Context.sendStickyBroadcast();
2、activity的关闭方法：finish(),finishActivity();服务的关闭方法：自己调用stopSelf(),Context.stopService();

3、启动activity时：关键intent标记：   Intent.FLAG_ACTIVITY_NEW_TASK,
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP,
                                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP;
                  关键<activity>属性：
                                  taskAffinity
                                  launchMode
                                  allowTaskReparenting
                                  clearTaskOnLaunch
                                  alwaysRetainTaskState
                                  finishOnTaskLaunch

4、affinity在两种情况下生效：加载activity的intent对象包含了FLAG_ACTIVITY_NEW_TASK标记
                            activity的allowTaskReparenting属性设置为true

5、、<activity>的launchModel：standard、singleTop、singleTask、singleInstance;
6、清理堆栈：alwaysRetainTaskState、clearTaskOnLaunch、finishOnTaskLaunch;
7、线程在代码中是以标准Java Thread对象创建的。
    Android提供了很多便于线程管理的类：Looper用于在一个线程中运行一个消息循环，
    Handler用于处理消息，HandlerThread用于使用一个消息循环启动一个线程；
8、onPause()是唯一一个在进程被杀死之前必然会调用的方法，onStop()和onDestroy()有可能不被执行。
9、onSaveInstanceState：Android在activity有可能被销毁之前（即onPause()调用之前）会调用此方法
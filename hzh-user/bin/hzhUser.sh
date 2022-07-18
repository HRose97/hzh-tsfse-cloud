#!/bin/sh
# author name：zanglikun
# author QQ：740969606

#	$0 是文件名
#	$1 是第一个参数

# echo $0 start 是启动服务
# echo $0 stop 是停止服务
# echo $0 restart 是重启服务
# echo $0 status 是查看服务状态

# 指定JAR包文件夹，将来运行的时候，此文件需要与jar包同级
AppName=你的jar名字.jar

# JVM参数
JVM_OPTS="-Dname=$AppName  -Duser.timezone=Asia/Shanghai -Xms512M -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC -Dspring.profiles.active=pro -Dserver.port=18083"
# -Dspring.profiles.active=pro 指的是激活application-pro.properties配置文件
# -Dserver.port=18083 可指定运行端口

# 此变量会获取到当前路径 等价于 Linux的pwd命令
APP_HOME=`pwd`

# 日志路径 一定要有此路经的文件夹，没有此路径的文件夹，将不会有日志文件：$AppName.log，记得创建logs文件夹。
LOG_PATH=$APP_HOME/logs/$AppName.log

# 获取第一个参数 如果为空，将会提示这个
if [ "$1" = "" ];
then
    echo -e "\033[0;31m 未输入操作名 \033[0m  \033[0;34m {start|stop|restart|status} \033[0m"
    exit 1
fi

# 如果没有指定JAR包，将会提示这个
if [ "$AppName" = "" ];
then
    echo -e "\033[0;31m 未输入应用名 \033[0m"
    exit 1
fi

# start方法
function start()
{
    # 获取PID，最后打印出结果$2 就是PID 注意，此处$2不是我们运行此脚本的第二个参数($0 不算做第一个参数)！
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`

	if [ x"$PID" != x"" ]; then
	    echo "$AppName is running..."
	else
		nohup java -jar  $JVM_OPTS $AppName > $LOG_PATH 2>&1 &
		# 下面可以查看一下命令对不对
		#echo	"nohup java -jar  $JVM_OPTS $AppName > $LOG_PATH 2>&1 &"
		echo "Start $AppName success..."
	fi
}

# stop方法
function stop()
{
    echo "Stop $AppName"

	PID=""
	# 定制查询服务PID的方法
	query(){
		PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
	}
	# 第一次查询状态，更变PID
	query

	if [ x"$PID" != x"" ]; then
		kill -TERM $PID
		echo "$AppName (pid:$PID) exiting..."
		while [ x"$PID" != x"" ]
		do
			# 间隔1秒执行后续代码
			sleep 1
			# 这是一个死循环，检查每一次PID，都会更变PID的最新结果，如果PID等于空了，就结束循环，输出结束
			query
		done
		echo "$AppName exited."
	else
		echo "$AppName already stopped."
	fi
}

# restart方法 注意方法内部的内容：调用了2个别的方法，所以从脚本加载从上倒下的顺序上，这2个被调用的方法，都要在此方法之前！
function restart()
{
    # 先停止服务，然后等待2秒后，再次重启服务，不过2秒时间太短，建议久一点
    stop
    sleep 2
    start
}

# status方法
function status()
{
    # 获取PID
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|wc -l`

    # 如果PID为0，代表服务器停止，不为0，就代表服务器正在运行
    if [ $PID != 0 ];then
        echo "$AppName is running..."
    else
        echo "$AppName is not running..."
    fi
}

# 获取第一个参数，执行不同的方法！
case $1 in
    start)
     # 执行start方法
    	start
    	;;
    stop)
     # 执行stop方法
    	stop
    	;;
    restart)
     # 执行resrart方法
    	restart
    	;;
    status)
     # 执行status方法
    	status
    	;;
    *)
# case的结束标志
esac
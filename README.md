# mybatis-crashlog-interceptor
Have you ever wanted to log your SQL only  when your system is in any trouble?  
This interceptor is going to allow your application to output SQL with parameters when MyBatis Mapper run into some troubles.

# Notes
This interceptor is not completed yet, use at your own risk.

# How to use

To use the interceptor, add configuration like belows to your mybatis configuration file.

```
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE configuration PUBLIC
  "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<plugins>
		<plugin interceptor="com.github.hljklsgfag.mybatis.CrashLogInterceptor" />		
	</plugins>
</configuration>
```

# Roadmap

* add unit test code.
* configuration for logger and log formats.
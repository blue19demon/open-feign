# open-feign
#### How to get a Git project into your build:
#### Step 1. Add the JitPack repository to your build file
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
#### Step 2. Add the dependency
               <dependency>
			<groupId>com.github.blue19demon.open-feign</groupId>
			<artifactId>feign-starter</artifactId>
			<version>2.0.0.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>com.github.blue19demon.open-feign</groupId>
			<artifactId>feign-starter-zk</artifactId>
			<version>2.0.0.RELEASE</version>
		</dependency>
#### Share this release:

#### TweetLink
#### That's it! The first time you request a project JitPack checks out the code, builds it and serves the build artifacts (jar, aar).
#### If the project doesn't have any GitHub Releases you can use the short commit hash or 'master-SNAPSHOT' as the version.

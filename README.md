Introduction:
1. This is a SpringBoot project, it is the part of the database of a online comic-con website.

2. It allows users to register to product they want the show, the information of their team,
   and the size of the spot they need etc.

3. It includes a server and MySQL database (using JPA).

4. It is a test-driven development, each database table is tested by a unit test.

5. It's a project I did as a backend developer in for a commercial game development group called crestruction.

Setting up:
1. This is a maven project, all dependencies are inside pom.xml, please install all of them.

2. This project use SpringBoot framework and MySQL database.

3. The setting for database is inside src/main/resources/application.properties, there are 2 options.

4. The first option is to use MySQL, (more complicated, not recommended) please install MySQL before
   running the project. Comment out line 1-3 in application.properties, use line 5-14. Comment out
   "@Rollback(false)" and "clear()" function of each unit test file.

5. The second option is to use H2 simulator, it does not use the real database but simulate the performance
   of database, no need to install anything. (recommended) Comment out line 5-8 in application.properties,
   use line 1-3 & 10-14. No need to change anything in the unit test files

6. After setting everything, run unit tests.

Instruction:
1. Database is constructed by JPA, related files are inside src/main/java/com/crestruction/web/database.

2. The project is test-driven project, unit tests are inside src/test/java/com/crestruction/web.

3. Tests includes addition, edition, deletion, searching of each table.You can run each test.

4. The structure of the database is inside the structure.jpg.

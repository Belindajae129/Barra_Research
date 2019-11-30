# Barra_Research
## 1. First build environment for Java, Axis2, and ANT

env | grep HOME

AXIS2_HOME=/opt/axis2

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-13.0.1.jdk/Contents/Home

HOME=/Users/belindajae

## 2. Make a working directory
mkdir bdti
cd bdti/

Download the BDT xml from the website and move it to the working directory
mv ~/Downloads/BDTService.xml bdt.wsdl

vi bdt.wsdl

/opt/axis2/bin/wsdl2java.sh -uri bdt.wsdl -d xmlbeans -ss -g -p com.barra.cp.bdtbeans -o ./

ls -l

All the java scripts are saved in the following dir:
ll src/com/barra/cp/bdtbeans/

ll src/com/barra/cp/bdtbeans/

ls
There are four files as below:
bdt.wsdl	build.xml	resources	src

sudo mv ~/Downloads/apache-ant-1.9.14 /opt/ant

After building successfully:
ls
There are five files as below:
bdt.wsdl	build		build.xml	resources src

cd src/
ls
There are two files:
com	org

cd ..
vi src/Example.java
/opt/ant/bin/ant 
ls
/opt/axis2/bin/axis2
/opt/axis2/bin/axis2.sh 
ls -l build/lib/

## 3. Java codes should be saved in src
/opt/ant/bin/ant
Compile the java scripts
/opt/axis2/bin/axis2.sh -cp ~/bdti/build/lib/BDTService.aar:~/bdti/build/lib/XBeans-packaged.jar Example2
Use axis to run the job

















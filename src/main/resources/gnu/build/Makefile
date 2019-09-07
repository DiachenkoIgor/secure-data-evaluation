Includes := -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/win32" -I/GMP/include -I.
LIBS := -L/GMP/lib -lgmp -L./include -ljcl 
CFLAGS := -Wall -shared -fPIC -Wl,-rpath,`pwd`
JAVA_FLAGS := -Xlint

all:GMP.c libjcl
	x86_64-w64-mingw32-gcc $(Includes)  GMP.c -o libnativegmp.dll $(LIBS) $(CFLAGS)


libjcl:jcl.c
	x86_64-w64-mingw32-gcc  -c $(Includes) jcl.c -o ./include/libjcl.a 

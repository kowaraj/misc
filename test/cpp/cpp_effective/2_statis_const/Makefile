clean:
	rm ./test ./gp.o
objs:
	g++-mp-4.8 -std=c++11 -c GamePlayer.cpp -o gp.o
all: clean objs
	g++-mp-4.8 -std=c++11 main.cpp gp.o -o test



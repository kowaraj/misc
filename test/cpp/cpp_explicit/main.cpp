#include <iostream>

class MyString {
public:
	explicit MyString(int i) { std::cout << "ctor(int)" << std::endl; }
	explicit MyString(const char* p) { std::cout << "ctor(char)" << std::endl; }

};


int main() {

	char c = 'a';
	char* pc = &c;
	MyString s1(pc);
	MyString s2(c);

	MyString s3 = pc;
	//MyString s4 = c;
}

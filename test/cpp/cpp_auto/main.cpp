#include <iostream>
#include <cassert>

int main() {
	std::cout << "test" << std::endl;

	int x = int();
	assert(x == 0);

	const int& crx = x;
	x = 42;
	assert(crx == 42 && x == 42);

	auto something = crx; // !! something is supposed to be const ref to x?
	assert(something == 42 && crx == 42 && x == 42);
	something = 43; // ?? something is neither const...
	assert(something == 43 && crx == 42 && x == 42); // ... nor a reference!
	// ... it's int !
	
}

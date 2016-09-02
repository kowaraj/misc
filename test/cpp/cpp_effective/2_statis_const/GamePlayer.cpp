#include <iostream>
#include <cassert>

#include "GamePlayer.h"

const int GamePlayer::NumTurns;

void GamePlayer::show() {
	std::cout << "p = " << this->NumTurns << std::endl;
	std::cout << "&p = " << &this->NumTurns << std::endl;
	
}


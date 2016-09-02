class GamePlayer {

private:
	static const int NumTurns = 5; // declaration !

	// 1. Error:
	// static const int NumTurns_oldCompiler; // old compiler doesn't allow definition here
	// int scores[NumTurns_oldCompiler]; // compilation error: not defined

	// 2. Solution:
	enum  { NumTurns_oldCompiler = 5 } ; // old compiler doesn't allow definition here
	int scores[NumTurns_oldCompiler]; // compilation error: not defined
	
	
public:
	void show();
};
	


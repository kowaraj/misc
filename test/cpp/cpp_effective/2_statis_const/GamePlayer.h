class GamePlayer {

private:
	static const int NumTurns = 5; // declaration !
	static const int NumTurns_oldCompiler; // old compiler doesn't allow definition here
	int scores[NumTurns_oldCompiler]; // compilation error: not defined
	
public:
	void show();
};
	


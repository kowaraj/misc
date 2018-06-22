#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <string>
#include <algorithm>
#include <dirent.h>

using namespace std;

int main(int argc, char** argv)
{
    if (argc < 3) {
        cerr << "Usage: " << argv[0] << " logs-folder string-search" << endl;
        return 1;
    }
    const string logs_folder = argv[1];
    const string string_search = argv[2];

    // open *read.log files
    vector<string> list_files(0);
    DIR *dir;
    struct dirent *ent;
    if ((dir = opendir((const char*)logs_folder.c_str())) != NULL) {
        while ((ent = readdir(dir)) != NULL) {
            string name = ent->d_name;
            if (name.find("read.log") == 8) {
                list_files.push_back(name);
            }
        }
        closedir (dir);
    } else {
        cerr << "Could not open directory " << logs_folder << endl;
        return 2;
    }

    // open output file for write
    string log_file = string_search;
    replace(log_file.begin(), log_file.end(), '/', '-');
    log_file = log_file + ".log";
    ofstream s_log_file;
    s_log_file.open(log_file);
    if (!s_log_file.good()) {
        cerr << "Could not open lof file " << log_file << endl;
        return 3;
    }

    cerr << "Start search '" << string_search << "' in folder " << logs_folder << endl;

    map<string, string> result;
    for(auto &file : list_files) {
        cerr << "Process file " << logs_folder << file << endl;

        // check tape.log file exists
        string tapes_file = file;
        string tapes;
        tapes_file.replace(8, 4, "tapes");
        ifstream s_tapes_file;
        s_tapes_file.open(logs_folder + tapes_file);
        if (s_tapes_file.good()) {
            for (string line; getline(s_tapes_file, line); ) {
                tapes += line + ',';
            }
        } else {
            cerr << "Could not open tapes file " << tapes_file << endl;
            continue;
        }
        
        ifstream s_read_file;
        s_read_file.open(logs_folder + file);
        if (s_read_file.good()) {
            for (string line; getline(s_read_file, line); ) {
                vector<string> sp;
                string::size_type pos, last_pos;
                pos = last_pos = 0;
                while ((pos = line.find_first_of(" ", pos)) != string::npos) {
                    string word = line.substr(last_pos, pos - last_pos);
                    if (word != "") {
                        sp.push_back(word);
                    }
                    pos++;
                    last_pos = pos;
                }
                sp.push_back(line.substr(last_pos));
                line = sp.back();

                // skip deframing.log
                if (line.find("deframing.log") != string::npos) {
                    continue;
                }
                
                // search needed string
                if (line.find(string_search) != string::npos) {
                    // remove parent directory if needed
                    if (line.find("./CHD") == 0) {
                        line = '.' + line.substr(5);
                    } else if (line.find("./BLOCKS") == 0) {
                        line = '.' + line.substr(8);
                    } else if (line.find("./FRAMES") == 0) {
                        line = '.' + line.substr(8);
                    }
                    //cout << line << " " << string_search << endl;
                    result.insert({line,tapes});
                }
            }
        } else {
            cerr << "Could not open file " << logs_folder << file << endl;
            return 1;
        }
        s_read_file.close();
    }

    for(auto const &f : result) {
        s_log_file << f.first << " " << f.second << endl;
    }
    s_log_file.close();

    return 0;
}

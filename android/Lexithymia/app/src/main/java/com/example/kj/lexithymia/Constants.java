package com.example.kj.lexithymia;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kapashnin on 10.09.18.
 */

public class Constants {

    //    public static final String SERVER = "http://ec2-34-244-223-173.eu-west-1.compute.amazonaws.com:3000/";
    public static final String SERVER = "http://34.244.223.173:3000/";
    public static final String JSON_URL = SERVER + "feelings";
    public static final String JSON_URL_UPDATE = SERVER + "feelings";

    public class PERMA {
        public static final String P = "positive, P";
        public static final String E = "engagement, E";
        public static final String R = "relationships, R";
        public static final String M = "meaning, M";
        public static final String A = "accomplishment, A";
    }

    public class PERMA_OPP {
        public static final String N = "negative, -P";
        public static final String D = "bored, -E";
        public static final String L = "lonely,   -R";
        public static final String U = "useless,  -M";
        public static final String F = "failure,  -A";
    }

    public class FACS {
        public static final String AU_6_12 = "happiness, happy";
        public static final String AU_1_4_15 = "sadness, sad";
        public static final String AU_1_2_5B_26 = "surprise, surprised";
        public static final String AU_1_2_4_5_7_20_26 = "fear, scared";
        public static final String AU_4_5_7_16 = "anger, angry";
        public static final String AU_9_15_16 = "disgust, disgusted";
        public static final String AU_R12A_R14A = "contempt, scornful";
    }

    public class EKMAN_EXT {
        public static final String Amusement = "amusement, amused";
        public static final String Contentment = "contentment, content";
        public static final String Embarrassment = "embarrassment, embarrassed";
        public static final String Excitement = "excitement, excited";
        public static final String Guilt = "guilt, guilty";
        public static final String Pride = "pride, proud";
        public static final String Relief = "relief, relieved";
        public static final String Satisfaction = "satisfaction, satisfied";
        public static final String Shame = "shame, ashamed";
        public static final String Pleasure = "pleasure, pleased";
        public static final String Pain = "pain, painful";
    }

    public class EXTRA {
        // physical problems
        public static final String Pain = "pain, in pain";
        public static final String Sick = "sick, sick";
        public static final String Tired = "tired, tired";
        // phychiatry
        public static final String Worry = "worry, worried";
        public static final String Anxiety = "anxiety, anxious";
        public static final String Stress = "stress, stressed out";
        // need for basic stuff
        public static final String Sleepy = "sleepy, sleepy";
        public static final String Hungry = "hungry, hungry";
        public static final String Horny = "horny, horny";
    }

    public static class PLUTCHIK {

        public static final String[] Optimism       = {"#FE010000", "#aaFFa7a7", "Aggressiveness", "Anticipation", "Anger"};


        public static final String[] Vigilance      = {"#FF010200", "#FF008a00", "Vigilance",      "",             ""};
        public static final String[] Anticipation   = {"#FF010100", "#FF00FF00", "Anticipation",   "",             ""};
        public static final String[] Interest       = {"#FF010000", "#FF9bff9b", "Interest",       "",             ""};

        public static final String[] Aggressiveness = {"#FE010000", "#AAFFA7A7", "Aggressiveness", "Anticipation", "Anger"};

        public static final String[] Rage           = {"#FF080200", "#FF880000", "Rage",           "",              ""};
        public static final String[] Anger          = {"#FF080100", "#FFFF0000", "Anger",          "",              ""};
        public static final String[] Annoyance      = {"#FF080000", "#FFFFa7a7", "Annoyance",      "",              ""};

        public static final String[] Contemp        = {"#FE080000", "#aaFFa7a7", "Contempt",       "Anger",        "Disgust"};


        public static final String getDiad(String e1, String e2) {
            int x = Arrays.asList(BasicEmotions).indexOf(e1);
            int y = Arrays.asList(BasicEmotions).indexOf(e2);
            return Diads[x][y];
        }

        private static final String[] BasicEmotions = {"Emotions: ", "Anger", "Anticipation", "Joy", "Trust", "Fear", "Surprise", "Sadness", "Disgust"};

        private static final String[][] Diads   = {
                //{"Emotions"       , "Anger"            , "Anticipation"    , "Joy"             , "Trust"           , "Fear"            , "Surprise"        , "Sadness"         , "Disgust"       },
                BasicEmotions,
                {"Anger"          , "."                , "Aggressiveness"  , "Pride"           , "Dominance"       , "-"               , "Outrage"         , "Envy"            , "Contempt"      },
                {"Anticipation"   , "x"                , "."               , "Optimism"        , "Fatalism"        , "Anxiety"         , "-"               , "Pessimism"       , "Anticipation"  },
                {"Joy"            , "x"                , "x"               , "."               , "Love"            , "Guilt"           , "Delight"         , "-"               , "Morbidness"    },
                {"Trust"          , "x"                , "x"               , "x"               , "."               , "Submission"      , "Curiosity"       , "Sentimentality"  , "-"             },
                {"Fear"           , "x"                , "x"               , "x"               , "x"               , "."               , "Alarm"           , "Despair"         , "Shame"         },
                {"Surprise"       , "x"                , "x"               , "x"               , "x"               , "x"               , "."               , "Disappointment"  , "\\?"           },
                {"Sadness"        , "x"                , "x"               , "x"               , "x"               , "x"               , "x"               , "."               , "Remorse"       },
                {"Disgust"        , "x"                , "x"               , "x"               , "x"               , "x"               , "x"               , "x"               , "."             }
        };

        public static final String R0 = "#FFFFA7A7"; // format: aRGB (Red)
        public static final String R1 = "#FFFF0000"; // format: aRGB (Red)
        public static final String R2 = "#FF880000"; // format: aRGB (Red, Dark)

        public static final String B0 = "#FFABABFF"; // format: aRGB (Blue)
        public static final String B1 = "#FFFF0000"; // format: aRGB (Blue)
        public static final String B2 = "#FF000081"; // format: aRGB (Blue, Dark)

        public static final String G0 = "#FF9BFF9B"; // format: aRGB (Green)
        public static final String G1 = "#FF00FF00"; // format: aRGB (Green)
        public static final String G2 = "#FF008A00"; // format: aRGB (Green, Dark)

        public static final String E0 = "#FFCCCCCC"; // format: aRGB (grEy)
        public static final String E1 = "#FF636363"; // format: aRGB (grEy)
        public static final String E2 = "#FF292929"; // format: aRGB (grEy, Dark)

        public static final String M0 = "#FFFFAEFF"; // format: aRGB (Magenta)
        public static final String M1 = "#FFFF00FF"; // format: aRGB (Magenta)
        public static final String M2 = "#FF740074"; // format: aRGB (Magenta, Dark)

        public static final String C0 = "#FFB9FFFF"; // format: aRGB (Cyan)
        public static final String C1 = "#FF00FFFF"; // format: aRGB (Cyan)
        public static final String C2 = "#FF008181"; // format: aRGB (Cyan, Dark)

        public static final String Y0 = "#FFFFFFB2"; // format: aRGB (Yellow)
        public static final String Y1 = "#FFFFFF00"; // format: aRGB (Yellow)
        public static final String Y2 = "#FF848400"; // format: aRGB (Yellow, Dark)

        public static final String N0 = "#FFFFBB79"; // format: aRGB (oraNge)
        public static final String N1 = "#FFFF7D00"; // format: aRGB (oraNge)
        public static final String N2 = "#FFA45100"; // format: aRGB (oraNge, Dark)

        public static final String XRB0 = "#FFD5A9D3"; // format: aRGB (R0 + B0)
        public static final String XBE0 = "#FFBBBBE6"; // format: aRGB (B0 + E0)
        public static final String XEM0 = "#FFE4BEE4"; // format: aRGB (E0 + M0)
        public static final String XMC0 = "#FFDDD6FF"; // format: aRGB (M0 + C0)
        public static final String XCY0 = "#FFDDFFD8"; // format: aRGB (C0 + Y0)
        public static final String XYN0 = "#FFFFDB94"; // format: aRGB (Y0 + N0)
        public static final String XNG0 = "#FFCDDD8A"; // format: aRGB (N0 + G0)
        public static final String XGR0 = "#FFCDD3A1"; // format: aRGB (G0 + R0)


        public static final String[][] EmotionsColor = {

                {"Emotion"        , "ColorBackground"  , "Color"           , "Intense"         , "Mild"             },

                {"Vigilance"      , "#FF010200"        , G2                , ""                , ""                 },
                {"Anticipation"   , "#FF010100"        , G1                , "Vigilance"       , "Interest"         },
                {"Interest"       , "#FF010000"        , G0                , ""                , ""                 },

                {"Aggressiveness" , "#FE010000"        , XGR0              , "Domination"      , "Disfavor"         }, // Intense+Mild are non-Plutchik

                {"Ecstasy"        , "#FF020200"        , N2                , ""                , ""                 },
                {"Joy"            , "#FF020100"        , N1                , "Ecstasy"         , "Serenity"         },
                {"Serenity"       , "#FF020000"        , N0                , ""                , ""                 },

                {"Optimism"       , "#FE020000"        , XNG0              , "Zeal"            , "Bemusement"       }, // Intense+Mild are non-Plutchik

                {"Admiration"     , "#FF030200"        , Y2                , ""                , ""                 },
                {"Trust"          , "#FF030100"        , Y1                , "Admiration"      , "Acceptance"       },
                {"Acceptance"     , "#FF030000"        , Y0                , ""                , ""                 },

                {"Love"           , "#FE030000"        , XYN0              , "Devotion"        , "Acknowledgement"  }, // Intense+Mild are non-Plutchik

                {"Terror"         , "#FF040200"        , C2                , ""                , ""                 },
                {"Fear"           , "#FF040100"        , C1                , "Terror"          , "Apprehension"     },
                {"Apprehension"   , "#FF040000"        , C0                , ""                , ""                 },

                {"Submission"     , "#FE040000"        , XCY0              , "Subservience"    , "Acquiescence"      }, // Intense+Mild are non-Plutchik

                {"Amazement"      , "#FF050200"        , M2                , ""                , ""                 },
                {"Surprise"       , "#FF050100"        , M1                , "Amazement"       , "Distraction"      },
                {"Distraction"    , "#FF050000"        , M0                , ""                , ""                 },

                {"Awe"            , "#FE050000"        , XMC0              , "Petrifaction"    , "Wariness"         }, // Intense+Mild are non-Plutchik

                {"Grief"          , "#FF060200"        , E2                , ""                , ""                 },
                {"Sadness"        , "#FF060100"        , E1                , "Grief"           , "Pensiveness"      },
                {"Pensiveness"    , "#FF060000"        , E0                , ""                , ""                 },

                {"Disapproval"    , "#FE060000"        , XEM0              , "Horror"          , "Dismay"           }, // Intense+Mild are non-Plutchik

                {"Loathing"       , "#FF070200"        , B2                , ""                , ""                 },
                {"Disgust"        , "#FF070100"        , B1                , "Loathing"        , "Boredom"          },
                {"Boredom"        , "#FF070000"        , B0                , ""                , ""                 },

                {"Remorse"        , "#FE070000"        , XBE0              , "Shame"           , "Listlessness"     }, // Intense+Mild are non-Plutchik

                {"Rage"           , "#FF080200"        , R2                , ""                , ""                 },
                {"Anger"          , "#FF080100"        , R1                , "Rage"            , "Annoyance"        },
                {"Annoyance"      , "#FF080000"        , R0                , ""                , ""                 },

                {"Contempt"       , "#FE080000"        , XRB0              , "Hatred"          , "Impatience"       }, // Intense+Mild are non-Plutchik

        };

        public static String[] getEmotionFromColor(String colorString) {
            String cs = colorString.toUpperCase();
            for (String[] s: EmotionsColor) {
                List<String> l = Arrays.asList(s);
                if (l.contains(cs))
                    return (String[]) l.toArray();
            }
            return null;
        }
        public static String getEmotionName(String[] cs){
            return cs[0];
        }
        public static String getEmotionColor(String[] cs){
            return cs[2];
        }
        public static String getEmotionColorBg(String[] cs){
            return cs[1];
        }
    }





    // more?
    // anticipating....
    // thoughtful...
    // annoyed, irritated...

    // add the timer!


    // raw description of a emotion -> to the basises

    //
}
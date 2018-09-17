package com.example.kj.lexithymia;

import java.lang.reflect.Array;
import java.util.Arrays;

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

        public static final String[] Aggressiveness = {"#FE010000", "#aaFFa7a7", "Aggressiveness", "Anticipation", "Anger"};

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

        public static final String[][] BasicEmotionColor = {


                {"Emotion"        , "ColorBackground"  , "Color"           , "Intense"         , "Mild"             },

                {"Optimism"       , "#FE020000"        , "mix"             , "Zeal"            , "Bemusement"       }, // Intense+Mild are non-Plutchik

                {"Vigilance"      , "#FF010200"        , "#FF008a00"       , ""                , ""                 },
                {"Anticipation"   , "#FF010100"        , "#FF00FF00"       , "Vigilance"       , "Interest"         },
                {"Interest"       , "#FF010000"        , "#FF9bff9b"       , ""                , ""                 },

                {"Aggressiveness" , "#FE010000"        , "mix"             , "Domination"      , "Disfavor"         }, // Intense+Mild are non-Plutchik

                {"Rage"           , "#FF080200"        , "#FF880000"       , ""                , ""                 },
                {"Anger"          , "#FF080100"        , "#FFFF0000"       , "Rage"            , "Annoyance"        },
                {"Annoyance"      , "#FF080000"        , "#FFFFa7a7"       , ""                , ""                 },


                {"Contemp"        , "#FE080000"        , "#aaFFa7a7"       , "Hatred"          , "Impatience"       }

        };
    }





    // more?
    // anticipating....
    // thoughtful...
    // annoyed, irritated...

    // add the timer!


    // raw description of a emotion -> to the basises

    //
}
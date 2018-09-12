package com.example.kj.lexithymia;

/**
 * Created by kapashnin on 10.09.18.
 */

public class Constants {

    //    public static final String SERVER = "http://ec2-34-244-223-173.eu-west-1.compute.amazonaws.com:3000/";
    public static final String SERVER = "http://34.244.223.173:3000/";
    public static final String JSON_URL = SERVER + "feelings";
    public static final String JSON_URL_UPDATE = SERVER + "feelingsUpdated";

    public class PERMA {
        public static final String P = "positive, P";
        public static final String E = "engagement, E";
        public static final String R = "relationships, R";
        public static final String M = "meaning, M";
        public static final String A = "accomplishment, A";
    }

    public class PERMA_OPP {
        public static final String N = "negative, -P";
        public static final String D = "detached, -E";
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
        public static final String Pain = "pain, in pain";
        public static final String Sick = "sick, sick";
        public static final String Tired = "tired, tired";
        public static final String Worry = "worry, worried";
        public static final String Anxiety = "anxiety, anxious";
    }
}
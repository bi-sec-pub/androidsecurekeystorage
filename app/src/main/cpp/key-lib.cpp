#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNIEXPORT
Java_com_bisec_securekeystorage_util_Keys_apiKey(JNIEnv *env, jobject object) {
    std::string api_key = "##thisisasupersecretapikeynooneshouldbeabletoseeit##";

    return env->NewStringUTF(api_key.c_str());
}
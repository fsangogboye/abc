/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_w3c_util_Unix */

#ifndef _Included_org_w3c_util_Unix
#define _Included_org_w3c_util_Unix
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: haslibrary */
/* Inaccessible static: that */
/*
 * Class:     org_w3c_util_Unix
 * Method:    libunix_getUID
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_w3c_util_Unix_libunix_1getUID
  (JNIEnv *, jobject, jstring);

/*
 * Class:     org_w3c_util_Unix
 * Method:    libunix_getGID
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_w3c_util_Unix_libunix_1getGID
  (JNIEnv *, jobject, jstring);

/*
 * Class:     org_w3c_util_Unix
 * Method:    libunix_setUID
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_w3c_util_Unix_libunix_1setUID
  (JNIEnv *, jobject, jint);

/*
 * Class:     org_w3c_util_Unix
 * Method:    libunix_setGID
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_w3c_util_Unix_libunix_1setGID
  (JNIEnv *, jobject, jint);

/*
 * Class:     org_w3c_util_Unix
 * Method:    libunix_chRoot
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_w3c_util_Unix_libunix_1chRoot
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif

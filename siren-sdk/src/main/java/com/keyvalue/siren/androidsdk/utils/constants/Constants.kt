package com.keyvalue.siren.androidsdk.utils.constants

enum class SirenErrorTypes {
    /** Generic error. */
    ERROR,

    /** Configuration error. The method/parameters are incorrect or not supported. */
    CONFIG_ERROR,

    /** Network error. */
    NETWORK_ERROR,
}

enum class BulkUpdateType {
    MARK_AS_READ,
    MARK_AS_DELETED,
}

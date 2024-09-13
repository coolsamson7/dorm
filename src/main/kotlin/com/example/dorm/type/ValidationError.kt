package com.example.dorm.type

class ValidationError(val violations : List<TypeViolation>) : Error() {}
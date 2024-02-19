// IAidlRandomNumber.aidl
package com.example.server.provides.service;

// AIDL interface to request random number from different process
// take care you define same interface like on server side with equal package name
interface IAidlRandomNumber {
    int getRandomNumber();
}
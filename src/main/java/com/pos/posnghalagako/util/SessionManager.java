package com.pos.posnghalagako.util;

import com.pos.posnghalagako.model.UserAccount;

/**
 * Singleton that tracks the currently logged-in user.
 * Cleared on logout to prevent session leaks.
 */
public final class SessionManager {
    private static UserAccount currentUser;

    private SessionManager() {
    }

    public static void login(UserAccount user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static UserAccount getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }
}

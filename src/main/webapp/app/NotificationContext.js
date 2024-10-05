import React, { createContext, useContext, useState } from 'react';

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
  const [notifications, setNotifications] = useState([]);

  const addNotification = message => {
    setNotifications(prev => [...prev, message]);
    // Optionally, remove the notification after a few seconds
    // setTimeout(() => removeNotification(message), 3000);
  };

  const removeNotification = message => {
    setNotifications(prev => prev.filter(note => note !== message));
  };

  return <NotificationContext.Provider value={{ notifications, addNotification }}>{children}</NotificationContext.Provider>;
};

export const useNotifications = () => useContext(NotificationContext);

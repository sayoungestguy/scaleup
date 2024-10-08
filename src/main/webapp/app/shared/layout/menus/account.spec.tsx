// src/main/webapp/app/shared/layout/menus/account.spec.tsx
import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { AccountMenu } from './account';
import { legacy_createStore as createStore } from 'redux';
import { configureStore } from '@reduxjs/toolkit';
import rootReducer from './config/rootReducer';

describe('AccountMenu', () => {
  let mountedWrapper: string | null = null;

  // Mocking store with authentication and userProfile state
  const store = configureStore({
    reducer: rootReducer,
    preloadedState: {
      user: {
        isAuthenticated: true,
      },
      authentication: {
        account: { login: 'admin' } as any, // Mock account data here
        isAuthenticated: true,
      },
      userProfile: {
        entities: [], // Mock user profile data if needed
      },
    },
  });

  const authenticatedWrapper = () => {
    if (!mountedWrapper) {
      const { container } = render(
        <Provider store={store}>
          <MemoryRouter>
            <AccountMenu isAuthenticated />
          </MemoryRouter>
        </Provider>,
      );
      mountedWrapper = container.innerHTML;
    }
    return mountedWrapper;
  };

  const guestWrapper = () => {
    if (!mountedWrapper) {
      const { container } = render(
        <Provider store={store}>
          <MemoryRouter>
            <AccountMenu />
          </MemoryRouter>
        </Provider>,
      );
      mountedWrapper = container.innerHTML;
    }
    return mountedWrapper;
  };

  beforeEach(() => {
    mountedWrapper = null; // Reset to null
  });

  it('Renders an authenticated AccountMenu component', () => {
    const html = authenticatedWrapper();
    expect(html).not.toContain('/login');
    expect(html).toContain('/logout');
  });

  it('Renders a guest AccountMenu component', () => {
    const html = guestWrapper();
    expect(html).toContain('/login');
    expect(html).not.toContain('/logout');
  });
});

import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { AccountMenu } from './account';
import { legacy_createStore as createStore } from 'redux';
import rootReducer from './config/rootReducer';

describe('AccountMenu', () => {
  let mountedWrapper: string = '';
  const store = createStore(rootReducer, {
    user: { isAuthenticated: true, account: { login: 'admin' } },
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
    mountedWrapper = 'undefined';
  });

  it('Renders a authenticated AccountMenu component', () => {
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

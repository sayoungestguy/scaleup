import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { Provider } from 'react-redux'; // Import the Provider from react-redux
import { AccountMenu } from './account';
import { getStore } from './config/store'; // Import your store

describe('AccountMenu', () => {
  let mountedWrapper;
  const store = getStore(); // Initialize the Redux store

  const authenticatedWrapper = () => {
    if (!mountedWrapper) {
      const { container } = render(
        <Provider store={store}>
          {' '}
          {/* Wrap with Provider */}
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
          {' '}
          {/* Wrap with Provider */}
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
    mountedWrapper = undefined;
  });

  // All tests will go here

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

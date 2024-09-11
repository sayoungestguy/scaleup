import React, { useLayoutEffect, useState, useEffect } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';

// export const Logout = () => {
//   const authentication = useAppSelector(state => state.authentication);
//   const dispatch = useAppDispatch();

//   useLayoutEffect(() => {
//     dispatch(logout());
//     if (authentication.logoutUrl) {
//       window.location.href = authentication.logoutUrl;
//     } else if (!authentication.isAuthenticated) {
//       window.location.href = '/';
//     }
//   });

//   return (
//     <div className="p-5">
//       <h4>Logged out successfully!</h4>
//     </div>
//   );
// };

export const Logout = () => {
  const [isRedirecting, setIsRedirecting] = useState(false);
  const authentication = useAppSelector(state => state.authentication);
  const dispatch = useAppDispatch();

  useEffect(() => {
    const performLogout = async () => {
      dispatch(logout());
      setIsRedirecting(true); // Set flag to indicate redirection in progress

      // Introduce a delay to handle the error boundary clean-up
      await new Promise(resolve => setTimeout(resolve, 100)); // Adjust delay as needed

      if (authentication.logoutUrl) {
        window.location.href = authentication.logoutUrl;
      } else if (!authentication.isAuthenticated) {
        window.location.href = '/';
      }
    };

    performLogout();
  }, [dispatch, authentication]);

  return (
    <div className="p-5">
      <h4>Logged out successfully!</h4>
    </div>
  );
};

export default Logout;

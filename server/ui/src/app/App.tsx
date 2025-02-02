import "./App.css";
import React from "react";

import { DefaultLayout } from "./layout";
// import { AppRoutes } from "./Routes";
import { NotificationsProvider } from "./components/NotificationsContext";

import "@patternfly/patternfly/patternfly.css";
import "@patternfly/patternfly/patternfly-addons.css";

const App: React.FC = () => {
  return (
    // <Router>
        <NotificationsProvider>
          <DefaultLayout>
            {/*<AppRoutes />*/}
              routes
          </DefaultLayout>
        </NotificationsProvider>
    // </Router>
  );
};

export default App;

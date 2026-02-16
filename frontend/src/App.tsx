import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import '@ionic/react/css/core.css';
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';
import { Route } from 'react-router-dom';
import ClassList from './features/classes/pages/ClassList';
import { BookingForm } from './features/booking';

setupIonicReact({
  mode: 'md', // Material Design mode for consistency
});

function App() {
  return (
    <IonApp>
      <IonReactRouter>
        <IonRouterOutlet>
          <Route path="/" component={ClassList} exact />
          <Route path="/book/:classId" component={BookingForm} />
        </IonRouterOutlet>
      </IonReactRouter>
    </IonApp>
  );
}

export default App;

import { IonApp, IonRouterOutlet, IonReactRouter, setupIonicReact } from '@ionic/react';
import '@ionic/react/css/core.css';
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';
import { Route } from 'react-router-dom';
import ClassList from './pages/ClassList';
import BookingForm from './pages/BookingForm';

setupIonicReact();

function App(): JSX.Element {
  return (
    <IonApp>
      <IonReactRouter>
        <IonRouterOutlet>
          <Route path="/" element={<ClassList />} />
          <Route path="/book/:classId" element={<BookingForm />} />
        </IonRouterOutlet>
      </IonReactRouter>
    </IonApp>
  );
}

export default App;

import React from 'react';
import { IonList, IonItem, IonLabel } from '@ionic/react';
import { PageLayout } from '../../../components';

const AdminHome: React.FC = () => (
  <PageLayout title="Admin Dashboard">
    <IonList>
      <IonItem button routerLink="/admin/create-class">
        <IonLabel>
          <h2>Create New Class</h2>
          <p>Add a new gym class to the schedule</p>
        </IonLabel>
      </IonItem>
    </IonList>
  </PageLayout>
);

export default AdminHome;

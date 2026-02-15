import React from 'react';
import { IonItem, IonLabel } from '@ionic/react';
import type { GymClass } from '../types/api';

interface ClassListItemProps {
  gymClass: GymClass;
  onClick: () => void;
}

export const ClassListItem: React.FC<ClassListItemProps> = ({ gymClass, onClick }) => (
  <IonItem button onClick={onClick} detail>
    <IonLabel>
      <h2>{gymClass.name}</h2>
      <p>
        {gymClass.availableSpots} / {gymClass.totalCapacity} spots available
      </p>
    </IonLabel>
  </IonItem>
);

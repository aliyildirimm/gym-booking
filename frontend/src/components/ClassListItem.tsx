import React from 'react';
import { IonItem, IonLabel } from '@ionic/react';
import type { GymClass } from '../types/api';

interface ClassListItemProps {
  gymClass: GymClass;
  onClick: () => void;
}

export const ClassListItem: React.FC<ClassListItemProps> = ({ gymClass, onClick }) => {
  const handleClick = (e: React.MouseEvent) => {
    // Blur the button to prevent focus issues with aria-hidden during navigation
    (e.currentTarget as HTMLElement).blur();
    onClick();
  };

  return (
    <IonItem button onClick={handleClick} detail>
      <IonLabel>
        <h2>{gymClass.name}</h2>
        <p>
          {gymClass.availableSpots} / {gymClass.totalCapacity} spots available
        </p>
      </IonLabel>
    </IonItem>
  );
};

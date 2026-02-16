import React from 'react';
import { IonIcon } from '@ionic/react';
import { checkmarkCircle } from 'ionicons/icons';
import { PrimaryButton } from '../../../components';

interface ClassCreatedSuccessProps {
  onViewClasses: () => void;
  onCreateAnother: () => void;
}

export const ClassCreatedSuccess: React.FC<ClassCreatedSuccessProps> = ({
  onViewClasses,
  onCreateAnother,
}) => (
  <>
    <div className="ion-text-center ion-padding">
      <IonIcon
        icon={checkmarkCircle}
        style={{ fontSize: '64px' }}
        color="success"
      />
      <p>Class created successfully!</p>
    </div>
    <PrimaryButton onClick={onViewClasses}>View All Classes</PrimaryButton>
    <PrimaryButton onClick={onCreateAnother} fill="outline">
      Create Another Class
    </PrimaryButton>
  </>
);

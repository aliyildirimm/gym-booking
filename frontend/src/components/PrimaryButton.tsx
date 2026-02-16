import React from 'react';
import { IonButton, IonSpinner } from '@ionic/react';

interface PrimaryButtonProps {
  children: React.ReactNode;
  loading?: boolean;
  disabled?: boolean;
  expand?: 'block' | 'full';
  className?: string;
  type?: 'button' | 'submit';
  onClick?: (e: React.MouseEvent<HTMLIonButtonElement>) => void;
}

export const PrimaryButton: React.FC<PrimaryButtonProps> = ({
  children,
  loading = false,
  disabled = false,
  expand = 'block',
  className = 'ion-margin-top',
  type = 'button',
  onClick,
}) => (
  <IonButton
    type={type}
    expand={expand}
    className={className}
    disabled={disabled || loading}
    onClick={onClick}
  >
    {loading ? <IonSpinner name="crescent" /> : children}
  </IonButton>
);

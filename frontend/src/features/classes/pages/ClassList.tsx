import React from 'react';
import { IonList, useIonViewWillEnter } from '@ionic/react';
import { useHistory } from 'react-router-dom';
import { useClasses, type AsyncState } from '../../../hooks';
import {
  PageLayout,
  LoadingSpinner,
  ErrorMessage,
  EmptyState,
  ClassListItem,
} from '../../../components';
import { GymClass } from '../../../types/api';

const ClassList: React.FC = () => {
  const { state: classesState, classes, fetchClasses } = useClasses();

  // Refetch classes every time this page comes into view
  useIonViewWillEnter(() => {
    fetchClasses();
  });

  return <PageLayout title="Gym Classes"><ClasListInner classesState={classesState} classes={classes}/></PageLayout>;

};


const ClasListInner: React.FC<{ classesState: AsyncState<GymClass[]>, classes: GymClass[] }> = ({ classesState, classes }) => {
  const history = useHistory();

  if (classesState.status === 'loading' || classesState.status === 'idle') {
    return (
        <LoadingSpinner />
    );
  }

  if (classesState.status === 'error') {
    return (
        <ErrorMessage message={classesState.error} />
    );
  }

  return (
      <IonList>
        {classes.length === 0 ? (
          <EmptyState message="No classes available." />
        ) : (
          classes.map((c) => (
            <ClassListItem
              key={c.id}
              gymClass={c}
              onClick={() => history.push(`/book/${c.id}`)}
            />
          ))
        )}
      </IonList>
  );
};
export default ClassList;

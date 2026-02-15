import React, { useEffect, useState } from 'react';
import { IonList } from '@ionic/react';
import { useNavigate } from 'react-router-dom';
import { getClasses } from '../api';
import type { GymClass } from '../types/api';
import {
  PageLayout,
  LoadingSpinner,
  ErrorMessage,
  EmptyState,
  ClassListItem,
} from '../components';

const ClassList: React.FC = () => {
  const navigate = useNavigate();
  const [classes, setClasses] = useState<GymClass[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getClasses()
      .then((res) => {
        setClasses(res.data ?? []);
        setError(null);
      })
      .catch((err) => {
        setError(err.message ?? 'Failed to load classes');
        setClasses([]);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <PageLayout title="Gym Classes">
      {loading && <LoadingSpinner />}
      {error != null && <ErrorMessage message={error} />}
      {!loading && error == null && (
        <IonList>
          {classes.length === 0 ? (
            <EmptyState message="No classes available." />
          ) : (
            classes.map((c) => (
              <ClassListItem
                key={c.id}
                gymClass={c}
                onClick={() => navigate(`/book/${c.id}`)}
              />
            ))
          )}
        </IonList>
      )}
    </PageLayout>
  );
};

export default ClassList;

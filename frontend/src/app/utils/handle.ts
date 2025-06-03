import { Observable, catchError, of } from 'rxjs';

export function handle<T>(
  observable: Observable<T>,
  action: string,
  onSuccess: () => void
) {
  observable
    .pipe(
      catchError((err) => {
        alert(`Error ${action} task: ${err.message}`);
        return of(null);
      })
    )
    .subscribe(() => onSuccess());
}

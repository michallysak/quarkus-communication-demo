import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'validLength'
})
export class ValidLengthPipe<S, T extends string | S[]> implements PipeTransform {
  transform(value: T | undefined, min: number = 0, max: number = 100): boolean {
    if (typeof value === 'string') {
      value = value.trim() as T;
    }
    const length = value?.length ?? 0;
    return length >= min && length <= max;
  }
}

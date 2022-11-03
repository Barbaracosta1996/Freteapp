import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWhatsMessageBatch, NewWhatsMessageBatch } from '../whats-message-batch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWhatsMessageBatch for edit and NewWhatsMessageBatchFormGroupInput for create.
 */
type WhatsMessageBatchFormGroupInput = IWhatsMessageBatch | PartialWithRequiredKeyOf<NewWhatsMessageBatch>;

type WhatsMessageBatchFormDefaults = Pick<NewWhatsMessageBatch, 'id'>;

type WhatsMessageBatchFormGroupContent = {
  id: FormControl<IWhatsMessageBatch['id'] | NewWhatsMessageBatch['id']>;
  tipo: FormControl<IWhatsMessageBatch['tipo']>;
  waidTo: FormControl<IWhatsMessageBatch['waidTo']>;
  perfilID: FormControl<IWhatsMessageBatch['perfilID']>;
  status: FormControl<IWhatsMessageBatch['status']>;
  ofertaId: FormControl<IWhatsMessageBatch['ofertaId']>;
  tipoOferta: FormControl<IWhatsMessageBatch['tipoOferta']>;
};

export type WhatsMessageBatchFormGroup = FormGroup<WhatsMessageBatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WhatsMessageBatchFormService {
  createWhatsMessageBatchFormGroup(whatsMessageBatch: WhatsMessageBatchFormGroupInput = { id: null }): WhatsMessageBatchFormGroup {
    const whatsMessageBatchRawValue = {
      ...this.getFormDefaults(),
      ...whatsMessageBatch,
    };
    return new FormGroup<WhatsMessageBatchFormGroupContent>({
      id: new FormControl(
        { value: whatsMessageBatchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipo: new FormControl(whatsMessageBatchRawValue.tipo, {
        validators: [Validators.required],
      }),
      waidTo: new FormControl(whatsMessageBatchRawValue.waidTo, {
        validators: [Validators.required],
      }),
      perfilID: new FormControl(whatsMessageBatchRawValue.perfilID, {
        validators: [Validators.required],
      }),
      status: new FormControl(whatsMessageBatchRawValue.status, {
        validators: [Validators.required],
      }),
      ofertaId: new FormControl(whatsMessageBatchRawValue.ofertaId),
      tipoOferta: new FormControl(whatsMessageBatchRawValue.tipoOferta),
    });
  }

  getWhatsMessageBatch(form: WhatsMessageBatchFormGroup): IWhatsMessageBatch | NewWhatsMessageBatch {
    return form.getRawValue() as IWhatsMessageBatch | NewWhatsMessageBatch;
  }

  resetForm(form: WhatsMessageBatchFormGroup, whatsMessageBatch: WhatsMessageBatchFormGroupInput): void {
    const whatsMessageBatchRawValue = { ...this.getFormDefaults(), ...whatsMessageBatch };
    form.reset(
      {
        ...whatsMessageBatchRawValue,
        id: { value: whatsMessageBatchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WhatsMessageBatchFormDefaults {
    return {
      id: null,
    };
  }
}

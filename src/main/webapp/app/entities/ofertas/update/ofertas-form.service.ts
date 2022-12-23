import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOfertas, NewOfertas } from '../ofertas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOfertas for edit and NewOfertasFormGroupInput for create.
 */
type OfertasFormGroupInput = IOfertas | PartialWithRequiredKeyOf<NewOfertas>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOfertas | NewOfertas> = Omit<T, 'dataPostagem' | 'dataEntrega' | 'dataModificacao'> & {
  dataPostagem?: string | null;
  dataEntrega?: string | null;
  dataModificacao?: string | null;
};

type OfertasFormRawValue = FormValueOf<IOfertas>;

type NewOfertasFormRawValue = FormValueOf<NewOfertas>;

type OfertasFormDefaults = Pick<NewOfertas, 'id' | 'dataPostagem' | 'dataEntrega' | 'dataModificacao'>;

type OfertasFormGroupContent = {
  id: FormControl<OfertasFormRawValue['id'] | NewOfertas['id']>;
  dataPostagem: FormControl<OfertasFormRawValue['dataPostagem']>;
  quantidade: FormControl<OfertasFormRawValue['quantidade']>;
  tipoCarga: FormControl<OfertasFormRawValue['tipoCarga']>;
  localizacaoOrigem: FormControl<OfertasFormRawValue['localizacaoOrigem']>;
  localizacaoDestino: FormControl<OfertasFormRawValue['localizacaoDestino']>;
  dataColeta: FormControl<OfertasFormRawValue['dataColeta']>;
  dataEntrega: FormControl<OfertasFormRawValue['dataEntrega']>;
  dataModificacao: FormControl<OfertasFormRawValue['dataModificacao']>;
  dataFechamento: FormControl<OfertasFormRawValue['dataFechamento']>;
  status: FormControl<OfertasFormRawValue['status']>;
  tipoOferta: FormControl<OfertasFormRawValue['tipoOferta']>;
  tipoTransporte: FormControl<OfertasFormRawValue['tipoTransporte']>;
  destino: FormControl<OfertasFormRawValue['destino']>;
  origem: FormControl<OfertasFormRawValue['origem']>;
  perfil: FormControl<OfertasFormRawValue['perfil']>;
};

export type OfertasFormGroup = FormGroup<OfertasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OfertasFormService {
  createOfertasFormGroup(ofertas: OfertasFormGroupInput = { id: null }): OfertasFormGroup {
    const ofertasRawValue = this.convertOfertasToOfertasRawValue({
      ...this.getFormDefaults(),
      ...ofertas,
    });
    return new FormGroup<OfertasFormGroupContent>({
      id: new FormControl(
        { value: ofertasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataPostagem: new FormControl(ofertasRawValue.dataPostagem, {
        validators: [Validators.required],
      }),
      quantidade: new FormControl(ofertasRawValue.quantidade, {
        validators: [Validators.required, Validators.min(1)],
      }),
      tipoCarga: new FormControl(ofertasRawValue.tipoCarga, {
        validators: [Validators.required],
      }),
      localizacaoOrigem: new FormControl(ofertasRawValue.localizacaoOrigem, {
        validators: [Validators.required],
      }),
      localizacaoDestino: new FormControl(ofertasRawValue.localizacaoDestino, {
        validators: [Validators.required],
      }),
      dataColeta: new FormControl(ofertasRawValue.dataColeta),
      dataEntrega: new FormControl(ofertasRawValue.dataEntrega),
      dataModificacao: new FormControl(ofertasRawValue.dataModificacao),
      dataFechamento: new FormControl(ofertasRawValue.dataFechamento, {
        validators: [Validators.required],
      }),
      status: new FormControl(ofertasRawValue.status, {
        validators: [Validators.required],
      }),
      tipoOferta: new FormControl(ofertasRawValue.tipoOferta, {
        validators: [Validators.required],
      }),
      tipoTransporte: new FormControl(ofertasRawValue.tipoTransporte),
      destino: new FormControl(ofertasRawValue.destino, {
        validators: [Validators.required],
      }),
      origem: new FormControl(ofertasRawValue.origem, {
        validators: [Validators.required],
      }),
      perfil: new FormControl(ofertasRawValue.perfil, {
        validators: [Validators.required],
      }),
    });
  }

  createOfertasPortalFormGroup(ofertas: OfertasFormGroupInput = { id: null }): OfertasFormGroup {
    const ofertasRawValue = this.convertOfertasToOfertasRawValue({
      ...this.getFormDefaults(),
      ...ofertas,
    });
    return new FormGroup<OfertasFormGroupContent>({
      id: new FormControl(
        { value: ofertasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataPostagem: new FormControl(ofertasRawValue.dataPostagem),
      quantidade: new FormControl(ofertasRawValue.quantidade, {
        validators: [Validators.required, Validators.min(1)],
      }),
      tipoCarga: new FormControl(ofertasRawValue.tipoCarga, {
        validators: [Validators.required],
      }),
      localizacaoOrigem: new FormControl(ofertasRawValue.localizacaoOrigem, {
        validators: [Validators.required],
      }),
      localizacaoDestino: new FormControl(ofertasRawValue.localizacaoDestino, {
        validators: [Validators.required],
      }),
      dataColeta: new FormControl(ofertasRawValue.dataColeta),
      dataEntrega: new FormControl(ofertasRawValue.dataEntrega),
      dataModificacao: new FormControl(ofertasRawValue.dataModificacao),
      dataFechamento: new FormControl(ofertasRawValue.dataFechamento, {
        validators: [Validators.required],
      }),
      status: new FormControl(ofertasRawValue.status),
      tipoOferta: new FormControl(ofertasRawValue.tipoOferta, {
        validators: [Validators.required],
      }),
      tipoTransporte: new FormControl(ofertasRawValue.tipoTransporte),
      destino: new FormControl(ofertasRawValue.destino),
      origem: new FormControl(ofertasRawValue.origem),
      perfil: new FormControl(ofertasRawValue.perfil),
    });
  }

  createOfertasPortalFormGroupOriginal(ofertas: OfertasFormGroupInput = { id: null }): OfertasFormGroup {
    const ofertasRawValue = this.convertOfertasToOfertasRawValue({
      ...this.getFormDefaults(),
      ...ofertas,
    });
    return new FormGroup<OfertasFormGroupContent>({
      id: new FormControl(
        { value: ofertasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataPostagem: new FormControl(ofertasRawValue.dataPostagem),
      quantidade: new FormControl(ofertasRawValue.quantidade, {
        validators: [Validators.required, Validators.min(1)],
      }),
      tipoCarga: new FormControl(ofertasRawValue.tipoCarga, {
        validators: [Validators.required],
      }),
      localizacaoOrigem: new FormControl(ofertasRawValue.localizacaoOrigem, {
        validators: [Validators.required],
      }),
      localizacaoDestino: new FormControl(ofertasRawValue.localizacaoDestino, {
        validators: [Validators.required],
      }),
      dataColeta: new FormControl(ofertasRawValue.dataColeta),
      dataEntrega: new FormControl(ofertasRawValue.dataEntrega),
      dataModificacao: new FormControl(ofertasRawValue.dataModificacao),
      dataFechamento: new FormControl(ofertasRawValue.dataFechamento, {
        validators: [Validators.required],
      }),
      status: new FormControl(ofertasRawValue.status),
      tipoOferta: new FormControl(ofertasRawValue.tipoOferta, {
        validators: [Validators.required],
      }),
      tipoTransporte: new FormControl(ofertasRawValue.tipoTransporte),
      destino: new FormControl(ofertasRawValue.destino),
      origem: new FormControl(ofertasRawValue.origem),
      perfil: new FormControl(ofertasRawValue.perfil, {
        validators: [Validators.required],
      }),
    });
  }

  getOfertas(form: OfertasFormGroup): IOfertas | NewOfertas {
    return this.convertOfertasRawValueToOfertas(form.getRawValue() as OfertasFormRawValue | NewOfertasFormRawValue);
  }

  resetForm(form: OfertasFormGroup, ofertas: OfertasFormGroupInput): void {
    const ofertasRawValue = this.convertOfertasToOfertasRawValue({ ...this.getFormDefaults(), ...ofertas });
    form.reset(
      {
        ...ofertasRawValue,
        id: { value: ofertasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OfertasFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataPostagem: currentTime,
      dataEntrega: currentTime,
      dataModificacao: currentTime,
    };
  }

  private convertOfertasRawValueToOfertas(rawOfertas: OfertasFormRawValue | NewOfertasFormRawValue): IOfertas | NewOfertas {
    return {
      ...rawOfertas,
      dataPostagem: dayjs(rawOfertas.dataPostagem, DATE_TIME_FORMAT),
      dataEntrega: dayjs(rawOfertas.dataEntrega, DATE_TIME_FORMAT),
      dataModificacao: dayjs(rawOfertas.dataModificacao, DATE_TIME_FORMAT),
    };
  }

  private convertOfertasToOfertasRawValue(
    ofertas: IOfertas | (Partial<NewOfertas> & OfertasFormDefaults)
  ): OfertasFormRawValue | PartialWithRequiredKeyOf<NewOfertasFormRawValue> {
    return {
      ...ofertas,
      dataPostagem: ofertas.dataPostagem ? ofertas.dataPostagem.format(DATE_TIME_FORMAT) : undefined,
      dataEntrega: ofertas.dataEntrega ? ofertas.dataEntrega.format(DATE_TIME_FORMAT) : undefined,
      dataModificacao: ofertas.dataModificacao ? ofertas.dataModificacao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

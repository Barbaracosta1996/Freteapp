import { Injectable } from '@angular/core';
import { IOfertas, NewOfertas } from '../../entities/ofertas/ofertas.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from '../../config/input.constants';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOfertas for edit and NewOfertasFormGroupInput for create.
 */
type DivulgarFormGroupInput = IOfertas | PartialWithRequiredKeyOf<NewOfertas>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOfertas | NewOfertas> = Omit<T, 'dataPostagem' | 'dataEntrega' | 'dataModificacao' | 'dataFechamento'> & {
  dataPostagem?: string | null;
  dataEntrega?: string | null;
  dataModificacao?: string | null;
  dataFechamento?: string | null;
};

type DivulgarFormRawValue = FormValueOf<IOfertas>;

type NewDivulgarFormRawValue = FormValueOf<NewOfertas>;

type DivulgarFormDefaults = Pick<NewOfertas, 'id' | 'dataPostagem' | 'dataEntrega' | 'dataModificacao' | 'dataFechamento'>;

type DivulgarFormGroupContent = {
  id: FormControl<DivulgarFormRawValue['id'] | NewOfertas['id']>;
  dataPostagem: FormControl<DivulgarFormRawValue['dataPostagem']>;
  quantidade: FormControl<DivulgarFormRawValue['quantidade']>;
  tipoCarga: FormControl<DivulgarFormRawValue['tipoCarga']>;
  localizacaoOrigem: FormControl<DivulgarFormRawValue['localizacaoOrigem']>;
  localizacaoDestino: FormControl<DivulgarFormRawValue['localizacaoDestino']>;
  dataColeta: FormControl<DivulgarFormRawValue['dataColeta']>;
  dataEntrega: FormControl<DivulgarFormRawValue['dataEntrega']>;
  dataModificacao: FormControl<DivulgarFormRawValue['dataModificacao']>;
  dataFechamento: FormControl<DivulgarFormRawValue['dataFechamento']>;
  status: FormControl<DivulgarFormRawValue['status']>;
  tipoOferta: FormControl<DivulgarFormRawValue['tipoOferta']>;
  tipoTransporte: FormControl<DivulgarFormRawValue['tipoTransporte']>;
  perfil: FormControl<DivulgarFormRawValue['perfil']>;
};

export type DivulgarFormGroup = FormGroup<DivulgarFormGroupContent>;

@Injectable({
  providedIn: 'root',
})
export class OfertaDivulgacaoFormService {
  createOfertasFormGroup(ofertas: DivulgarFormGroupInput = { id: null }): DivulgarFormGroup {
    const ofertasRawValue = this.convertOfertasToOfertasRawValue({
      ...this.getFormDefaults(),
      ...ofertas,
    });
    return new FormGroup<DivulgarFormGroupContent>({
      id: new FormControl(
        { value: ofertasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataPostagem: new FormControl(
        { value: ofertasRawValue.dataPostagem, disabled: true },
        {
          validators: [Validators.required],
        }
      ),
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
      dataFechamento: new FormControl(ofertasRawValue.dataFechamento),
      status: new FormControl(ofertasRawValue.status),
      tipoOferta: new FormControl(ofertasRawValue.tipoOferta, {
        validators: [Validators.required],
      }),
      tipoTransporte: new FormControl(ofertasRawValue.tipoTransporte),
      perfil: new FormControl(ofertasRawValue.perfil),
    });
  }

  getOfertas(form: DivulgarFormGroup): IOfertas | NewOfertas {
    return this.convertOfertasRawValueToOfertas(form.getRawValue() as DivulgarFormRawValue | NewDivulgarFormRawValue);
  }

  resetForm(form: DivulgarFormGroup, ofertas: DivulgarFormGroupInput): void {
    const ofertasRawValue = this.convertOfertasToOfertasRawValue({ ...this.getFormDefaults(), ...ofertas });
    form.reset(
      {
        ...ofertasRawValue,
        id: { value: ofertasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DivulgarFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataPostagem: currentTime,
      dataEntrega: currentTime,
      dataModificacao: currentTime,
      dataFechamento: currentTime,
    };
  }

  private convertOfertasRawValueToOfertas(rawOfertas: DivulgarFormRawValue | NewDivulgarFormRawValue): IOfertas | NewOfertas {
    return {
      ...rawOfertas,
      dataPostagem: dayjs(rawOfertas.dataPostagem, DATE_TIME_FORMAT),
      dataEntrega: dayjs(rawOfertas.dataEntrega, DATE_TIME_FORMAT),
      dataModificacao: dayjs(rawOfertas.dataModificacao, DATE_TIME_FORMAT),
      dataFechamento: dayjs(rawOfertas.dataFechamento, DATE_TIME_FORMAT),
    };
  }

  private convertOfertasToOfertasRawValue(
    ofertas: IOfertas | (Partial<NewOfertas> & DivulgarFormDefaults)
  ): DivulgarFormRawValue | PartialWithRequiredKeyOf<NewDivulgarFormRawValue> {
    return {
      ...ofertas,
      dataPostagem: ofertas.dataPostagem ? ofertas.dataPostagem.format(DATE_TIME_FORMAT) : undefined,
      dataEntrega: ofertas.dataEntrega ? ofertas.dataEntrega.format(DATE_TIME_FORMAT) : undefined,
      dataModificacao: ofertas.dataModificacao ? ofertas.dataModificacao.format(DATE_TIME_FORMAT) : undefined,
      dataFechamento: ofertas.dataFechamento ? ofertas.dataFechamento.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

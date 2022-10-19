import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaVeiculoFormService } from './categoria-veiculo-form.service';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';
import { ICategoriaVeiculo } from '../categoria-veiculo.model';

import { CategoriaVeiculoUpdateComponent } from './categoria-veiculo-update.component';

describe('CategoriaVeiculo Management Update Component', () => {
  let comp: CategoriaVeiculoUpdateComponent;
  let fixture: ComponentFixture<CategoriaVeiculoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaVeiculoFormService: CategoriaVeiculoFormService;
  let categoriaVeiculoService: CategoriaVeiculoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaVeiculoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CategoriaVeiculoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaVeiculoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaVeiculoFormService = TestBed.inject(CategoriaVeiculoFormService);
    categoriaVeiculoService = TestBed.inject(CategoriaVeiculoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const categoriaVeiculo: ICategoriaVeiculo = { id: 456 };

      activatedRoute.data = of({ categoriaVeiculo });
      comp.ngOnInit();

      expect(comp.categoriaVeiculo).toEqual(categoriaVeiculo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaVeiculo>>();
      const categoriaVeiculo = { id: 123 };
      jest.spyOn(categoriaVeiculoFormService, 'getCategoriaVeiculo').mockReturnValue(categoriaVeiculo);
      jest.spyOn(categoriaVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaVeiculo }));
      saveSubject.complete();

      // THEN
      expect(categoriaVeiculoFormService.getCategoriaVeiculo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaVeiculoService.update).toHaveBeenCalledWith(expect.objectContaining(categoriaVeiculo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaVeiculo>>();
      const categoriaVeiculo = { id: 123 };
      jest.spyOn(categoriaVeiculoFormService, 'getCategoriaVeiculo').mockReturnValue({ id: null });
      jest.spyOn(categoriaVeiculoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaVeiculo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaVeiculo }));
      saveSubject.complete();

      // THEN
      expect(categoriaVeiculoFormService.getCategoriaVeiculo).toHaveBeenCalled();
      expect(categoriaVeiculoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaVeiculo>>();
      const categoriaVeiculo = { id: 123 };
      jest.spyOn(categoriaVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaVeiculoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

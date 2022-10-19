import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RotasOfertasFormService } from './rotas-ofertas-form.service';
import { RotasOfertasService } from '../service/rotas-ofertas.service';
import { IRotasOfertas } from '../rotas-ofertas.model';
import { IOfertas } from 'app/entities/ofertas/ofertas.model';
import { OfertasService } from 'app/entities/ofertas/service/ofertas.service';

import { RotasOfertasUpdateComponent } from './rotas-ofertas-update.component';

describe('RotasOfertas Management Update Component', () => {
  let comp: RotasOfertasUpdateComponent;
  let fixture: ComponentFixture<RotasOfertasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rotasOfertasFormService: RotasOfertasFormService;
  let rotasOfertasService: RotasOfertasService;
  let ofertasService: OfertasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RotasOfertasUpdateComponent],
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
      .overrideTemplate(RotasOfertasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RotasOfertasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rotasOfertasFormService = TestBed.inject(RotasOfertasFormService);
    rotasOfertasService = TestBed.inject(RotasOfertasService);
    ofertasService = TestBed.inject(OfertasService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ofertas query and add missing value', () => {
      const rotasOfertas: IRotasOfertas = { id: 456 };
      const ofertas: IOfertas = { id: 54007 };
      rotasOfertas.ofertas = ofertas;

      const ofertasCollection: IOfertas[] = [{ id: 13739 }];
      jest.spyOn(ofertasService, 'query').mockReturnValue(of(new HttpResponse({ body: ofertasCollection })));
      const additionalOfertas = [ofertas];
      const expectedCollection: IOfertas[] = [...additionalOfertas, ...ofertasCollection];
      jest.spyOn(ofertasService, 'addOfertasToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rotasOfertas });
      comp.ngOnInit();

      expect(ofertasService.query).toHaveBeenCalled();
      expect(ofertasService.addOfertasToCollectionIfMissing).toHaveBeenCalledWith(
        ofertasCollection,
        ...additionalOfertas.map(expect.objectContaining)
      );
      expect(comp.ofertasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rotasOfertas: IRotasOfertas = { id: 456 };
      const ofertas: IOfertas = { id: 65334 };
      rotasOfertas.ofertas = ofertas;

      activatedRoute.data = of({ rotasOfertas });
      comp.ngOnInit();

      expect(comp.ofertasSharedCollection).toContain(ofertas);
      expect(comp.rotasOfertas).toEqual(rotasOfertas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRotasOfertas>>();
      const rotasOfertas = { id: 123 };
      jest.spyOn(rotasOfertasFormService, 'getRotasOfertas').mockReturnValue(rotasOfertas);
      jest.spyOn(rotasOfertasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rotasOfertas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rotasOfertas }));
      saveSubject.complete();

      // THEN
      expect(rotasOfertasFormService.getRotasOfertas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rotasOfertasService.update).toHaveBeenCalledWith(expect.objectContaining(rotasOfertas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRotasOfertas>>();
      const rotasOfertas = { id: 123 };
      jest.spyOn(rotasOfertasFormService, 'getRotasOfertas').mockReturnValue({ id: null });
      jest.spyOn(rotasOfertasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rotasOfertas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rotasOfertas }));
      saveSubject.complete();

      // THEN
      expect(rotasOfertasFormService.getRotasOfertas).toHaveBeenCalled();
      expect(rotasOfertasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRotasOfertas>>();
      const rotasOfertas = { id: 123 };
      jest.spyOn(rotasOfertasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rotasOfertas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rotasOfertasService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOfertas', () => {
      it('Should forward to ofertasService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ofertasService, 'compareOfertas');
        comp.compareOfertas(entity, entity2);
        expect(ofertasService.compareOfertas).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

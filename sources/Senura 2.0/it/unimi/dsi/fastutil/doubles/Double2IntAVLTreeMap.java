/*      */ package it.unimi.dsi.fastutil.doubles;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
/*      */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*      */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*      */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Double2IntAVLTreeMap
/*      */   extends AbstractDouble2IntSortedMap
/*      */   implements Serializable, Cloneable
/*      */ {
/*      */   protected transient Entry tree;
/*      */   protected int count;
/*      */   protected transient Entry firstEntry;
/*      */   protected transient Entry lastEntry;
/*      */   protected transient ObjectSortedSet<Double2IntMap.Entry> entries;
/*      */   protected transient DoubleSortedSet keys;
/*      */   protected transient IntCollection values;
/*      */   protected transient boolean modified;
/*      */   protected Comparator<? super Double> storedComparator;
/*      */   protected transient DoubleComparator actualComparator;
/*      */   private static final long serialVersionUID = -7046029254386353129L;
/*      */   private transient boolean[] dirPath;
/*      */   
/*      */   public Double2IntAVLTreeMap() {
/*   70 */     allocatePaths();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   76 */     this.tree = null;
/*   77 */     this.count = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setActualComparator() {
/*   89 */     this.actualComparator = DoubleComparators.asDoubleComparator(this.storedComparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap(Comparator<? super Double> c) {
/*   98 */     this();
/*   99 */     this.storedComparator = c;
/*  100 */     setActualComparator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap(Map<? extends Double, ? extends Integer> m) {
/*  109 */     this();
/*  110 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap(SortedMap<Double, Integer> m) {
/*  120 */     this(m.comparator());
/*  121 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap(Double2IntMap m) {
/*  130 */     this();
/*  131 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap(Double2IntSortedMap m) {
/*  141 */     this(m.comparator());
/*  142 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap(double[] k, int[] v, Comparator<? super Double> c) {
/*  158 */     this(c);
/*  159 */     if (k.length != v.length) {
/*  160 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  162 */     for (int i = 0; i < k.length; i++) {
/*  163 */       put(k[i], v[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap(double[] k, int[] v) {
/*  176 */     this(k, v, (Comparator<? super Double>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int compare(double k1, double k2) {
/*  204 */     return (this.actualComparator == null) ? Double.compare(k1, k2) : this.actualComparator.compare(k1, k2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Entry findKey(double k) {
/*  216 */     Entry e = this.tree;
/*      */     int cmp;
/*  218 */     while (e != null && (cmp = compare(k, e.key)) != 0)
/*  219 */       e = (cmp < 0) ? e.left() : e.right(); 
/*  220 */     return e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Entry locateKey(double k) {
/*  232 */     Entry e = this.tree, last = this.tree;
/*  233 */     int cmp = 0;
/*  234 */     while (e != null && (cmp = compare(k, e.key)) != 0) {
/*  235 */       last = e;
/*  236 */       e = (cmp < 0) ? e.left() : e.right();
/*      */     } 
/*  238 */     return (cmp == 0) ? e : last;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void allocatePaths() {
/*  246 */     this.dirPath = new boolean[48];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addTo(double k, int incr) {
/*  265 */     Entry e = add(k);
/*  266 */     int oldValue = e.value;
/*  267 */     e.value += incr;
/*  268 */     return oldValue;
/*      */   }
/*      */   
/*      */   public int put(double k, int v) {
/*  272 */     Entry e = add(k);
/*  273 */     int oldValue = e.value;
/*  274 */     e.value = v;
/*  275 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Entry add(double k) {
/*  292 */     this.modified = false;
/*  293 */     Entry e = null;
/*  294 */     if (this.tree == null) {
/*  295 */       this.count++;
/*  296 */       e = this.tree = this.lastEntry = this.firstEntry = new Entry(k, this.defRetValue);
/*  297 */       this.modified = true;
/*      */     } else {
/*  299 */       Entry p = this.tree, q = null, y = this.tree, z = null, w = null;
/*  300 */       int i = 0; while (true) {
/*      */         int cmp;
/*  302 */         if ((cmp = compare(k, p.key)) == 0) {
/*  303 */           return p;
/*      */         }
/*  305 */         if (p.balance() != 0) {
/*  306 */           i = 0;
/*  307 */           z = q;
/*  308 */           y = p;
/*      */         } 
/*  310 */         this.dirPath[i++] = (cmp > 0); if ((cmp > 0)) {
/*  311 */           if (p.succ()) {
/*  312 */             this.count++;
/*  313 */             e = new Entry(k, this.defRetValue);
/*  314 */             this.modified = true;
/*  315 */             if (p.right == null)
/*  316 */               this.lastEntry = e; 
/*  317 */             e.left = p;
/*  318 */             e.right = p.right;
/*  319 */             p.right(e);
/*      */             break;
/*      */           } 
/*  322 */           q = p;
/*  323 */           p = p.right; continue;
/*      */         } 
/*  325 */         if (p.pred()) {
/*  326 */           this.count++;
/*  327 */           e = new Entry(k, this.defRetValue);
/*  328 */           this.modified = true;
/*  329 */           if (p.left == null)
/*  330 */             this.firstEntry = e; 
/*  331 */           e.right = p;
/*  332 */           e.left = p.left;
/*  333 */           p.left(e);
/*      */           break;
/*      */         } 
/*  336 */         q = p;
/*  337 */         p = p.left;
/*      */       } 
/*      */       
/*  340 */       p = y;
/*  341 */       i = 0;
/*  342 */       while (p != e) {
/*  343 */         if (this.dirPath[i]) {
/*  344 */           p.incBalance();
/*      */         } else {
/*  346 */           p.decBalance();
/*  347 */         }  p = this.dirPath[i++] ? p.right : p.left;
/*      */       } 
/*  349 */       if (y.balance() == -2) {
/*  350 */         Entry x = y.left;
/*  351 */         if (x.balance() == -1) {
/*  352 */           w = x;
/*  353 */           if (x.succ()) {
/*  354 */             x.succ(false);
/*  355 */             y.pred(x);
/*      */           } else {
/*  357 */             y.left = x.right;
/*  358 */           }  x.right = y;
/*  359 */           x.balance(0);
/*  360 */           y.balance(0);
/*      */         } else {
/*  362 */           assert x.balance() == 1;
/*  363 */           w = x.right;
/*  364 */           x.right = w.left;
/*  365 */           w.left = x;
/*  366 */           y.left = w.right;
/*  367 */           w.right = y;
/*  368 */           if (w.balance() == -1) {
/*  369 */             x.balance(0);
/*  370 */             y.balance(1);
/*  371 */           } else if (w.balance() == 0) {
/*  372 */             x.balance(0);
/*  373 */             y.balance(0);
/*      */           } else {
/*  375 */             x.balance(-1);
/*  376 */             y.balance(0);
/*      */           } 
/*  378 */           w.balance(0);
/*  379 */           if (w.pred()) {
/*  380 */             x.succ(w);
/*  381 */             w.pred(false);
/*      */           } 
/*  383 */           if (w.succ()) {
/*  384 */             y.pred(w);
/*  385 */             w.succ(false);
/*      */           } 
/*      */         } 
/*  388 */       } else if (y.balance() == 2) {
/*  389 */         Entry x = y.right;
/*  390 */         if (x.balance() == 1) {
/*  391 */           w = x;
/*  392 */           if (x.pred()) {
/*  393 */             x.pred(false);
/*  394 */             y.succ(x);
/*      */           } else {
/*  396 */             y.right = x.left;
/*  397 */           }  x.left = y;
/*  398 */           x.balance(0);
/*  399 */           y.balance(0);
/*      */         } else {
/*  401 */           assert x.balance() == -1;
/*  402 */           w = x.left;
/*  403 */           x.left = w.right;
/*  404 */           w.right = x;
/*  405 */           y.right = w.left;
/*  406 */           w.left = y;
/*  407 */           if (w.balance() == 1) {
/*  408 */             x.balance(0);
/*  409 */             y.balance(-1);
/*  410 */           } else if (w.balance() == 0) {
/*  411 */             x.balance(0);
/*  412 */             y.balance(0);
/*      */           } else {
/*  414 */             x.balance(1);
/*  415 */             y.balance(0);
/*      */           } 
/*  417 */           w.balance(0);
/*  418 */           if (w.pred()) {
/*  419 */             y.succ(w);
/*  420 */             w.pred(false);
/*      */           } 
/*  422 */           if (w.succ()) {
/*  423 */             x.pred(w);
/*  424 */             w.succ(false);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  428 */         return e;
/*  429 */       }  if (z == null) {
/*  430 */         this.tree = w;
/*      */       }
/*  432 */       else if (z.left == y) {
/*  433 */         z.left = w;
/*      */       } else {
/*  435 */         z.right = w;
/*      */       } 
/*      */     } 
/*  438 */     return e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Entry parent(Entry e) {
/*  448 */     if (e == this.tree) {
/*  449 */       return null;
/*      */     }
/*  451 */     Entry y = e, x = y;
/*      */     while (true) {
/*  453 */       if (y.succ()) {
/*  454 */         Entry p = y.right;
/*  455 */         if (p == null || p.left != e) {
/*  456 */           while (!x.pred())
/*  457 */             x = x.left; 
/*  458 */           p = x.left;
/*      */         } 
/*  460 */         return p;
/*  461 */       }  if (x.pred()) {
/*  462 */         Entry p = x.left;
/*  463 */         if (p == null || p.right != e) {
/*  464 */           while (!y.succ())
/*  465 */             y = y.right; 
/*  466 */           p = y.right;
/*      */         } 
/*  468 */         return p;
/*      */       } 
/*  470 */       x = x.left;
/*  471 */       y = y.right;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int remove(double k) {
/*  481 */     this.modified = false;
/*  482 */     if (this.tree == null) {
/*  483 */       return this.defRetValue;
/*      */     }
/*  485 */     Entry p = this.tree, q = null;
/*  486 */     boolean dir = false;
/*  487 */     double kk = k;
/*      */     int cmp;
/*  489 */     while ((cmp = compare(kk, p.key)) != 0) {
/*      */       
/*  491 */       if (dir = (cmp > 0)) {
/*  492 */         q = p;
/*  493 */         if ((p = p.right()) == null)
/*  494 */           return this.defRetValue;  continue;
/*      */       } 
/*  496 */       q = p;
/*  497 */       if ((p = p.left()) == null) {
/*  498 */         return this.defRetValue;
/*      */       }
/*      */     } 
/*  501 */     if (p.left == null)
/*  502 */       this.firstEntry = p.next(); 
/*  503 */     if (p.right == null)
/*  504 */       this.lastEntry = p.prev(); 
/*  505 */     if (p.succ())
/*  506 */     { if (p.pred())
/*  507 */       { if (q != null)
/*  508 */         { if (dir) {
/*  509 */             q.succ(p.right);
/*      */           } else {
/*  511 */             q.pred(p.left);
/*      */           }  }
/*  513 */         else { this.tree = dir ? p.right : p.left; }
/*      */          }
/*  515 */       else { (p.prev()).right = p.right;
/*  516 */         if (q != null)
/*  517 */         { if (dir) {
/*  518 */             q.right = p.left;
/*      */           } else {
/*  520 */             q.left = p.left;
/*      */           }  }
/*  522 */         else { this.tree = p.left; }
/*      */          }
/*      */        }
/*  525 */     else { Entry r = p.right;
/*  526 */       if (r.pred()) {
/*  527 */         r.left = p.left;
/*  528 */         r.pred(p.pred());
/*  529 */         if (!r.pred())
/*  530 */           (r.prev()).right = r; 
/*  531 */         if (q != null)
/*  532 */         { if (dir) {
/*  533 */             q.right = r;
/*      */           } else {
/*  535 */             q.left = r;
/*      */           }  }
/*  537 */         else { this.tree = r; }
/*  538 */          r.balance(p.balance());
/*  539 */         q = r;
/*  540 */         dir = true;
/*      */       } else {
/*      */         Entry s;
/*      */         while (true) {
/*  544 */           s = r.left;
/*  545 */           if (s.pred())
/*      */             break; 
/*  547 */           r = s;
/*      */         } 
/*  549 */         if (s.succ()) {
/*  550 */           r.pred(s);
/*      */         } else {
/*  552 */           r.left = s.right;
/*  553 */         }  s.left = p.left;
/*  554 */         if (!p.pred()) {
/*  555 */           (p.prev()).right = s;
/*  556 */           s.pred(false);
/*      */         } 
/*  558 */         s.right = p.right;
/*  559 */         s.succ(false);
/*  560 */         if (q != null)
/*  561 */         { if (dir) {
/*  562 */             q.right = s;
/*      */           } else {
/*  564 */             q.left = s;
/*      */           }  }
/*  566 */         else { this.tree = s; }
/*  567 */          s.balance(p.balance());
/*  568 */         q = r;
/*  569 */         dir = false;
/*      */       }  }
/*      */ 
/*      */     
/*  573 */     while (q != null) {
/*  574 */       Entry y = q;
/*  575 */       q = parent(y);
/*  576 */       if (!dir) {
/*  577 */         dir = (q != null && q.left != y);
/*  578 */         y.incBalance();
/*  579 */         if (y.balance() == 1)
/*      */           break; 
/*  581 */         if (y.balance() == 2) {
/*  582 */           Entry x = y.right;
/*  583 */           assert x != null;
/*  584 */           if (x.balance() == -1) {
/*      */             
/*  586 */             assert x.balance() == -1;
/*  587 */             Entry w = x.left;
/*  588 */             x.left = w.right;
/*  589 */             w.right = x;
/*  590 */             y.right = w.left;
/*  591 */             w.left = y;
/*  592 */             if (w.balance() == 1) {
/*  593 */               x.balance(0);
/*  594 */               y.balance(-1);
/*  595 */             } else if (w.balance() == 0) {
/*  596 */               x.balance(0);
/*  597 */               y.balance(0);
/*      */             } else {
/*  599 */               assert w.balance() == -1;
/*  600 */               x.balance(1);
/*  601 */               y.balance(0);
/*      */             } 
/*  603 */             w.balance(0);
/*  604 */             if (w.pred()) {
/*  605 */               y.succ(w);
/*  606 */               w.pred(false);
/*      */             } 
/*  608 */             if (w.succ()) {
/*  609 */               x.pred(w);
/*  610 */               w.succ(false);
/*      */             } 
/*  612 */             if (q != null) {
/*  613 */               if (dir) {
/*  614 */                 q.right = w; continue;
/*      */               } 
/*  616 */               q.left = w; continue;
/*      */             } 
/*  618 */             this.tree = w; continue;
/*      */           } 
/*  620 */           if (q != null)
/*  621 */           { if (dir) {
/*  622 */               q.right = x;
/*      */             } else {
/*  624 */               q.left = x;
/*      */             }  }
/*  626 */           else { this.tree = x; }
/*  627 */            if (x.balance() == 0) {
/*  628 */             y.right = x.left;
/*  629 */             x.left = y;
/*  630 */             x.balance(-1);
/*  631 */             y.balance(1);
/*      */             break;
/*      */           } 
/*  634 */           assert x.balance() == 1;
/*  635 */           if (x.pred()) {
/*  636 */             y.succ(true);
/*  637 */             x.pred(false);
/*      */           } else {
/*  639 */             y.right = x.left;
/*  640 */           }  x.left = y;
/*  641 */           y.balance(0);
/*  642 */           x.balance(0);
/*      */         } 
/*      */         continue;
/*      */       } 
/*  646 */       dir = (q != null && q.left != y);
/*  647 */       y.decBalance();
/*  648 */       if (y.balance() == -1)
/*      */         break; 
/*  650 */       if (y.balance() == -2) {
/*  651 */         Entry x = y.left;
/*  652 */         assert x != null;
/*  653 */         if (x.balance() == 1) {
/*      */           
/*  655 */           assert x.balance() == 1;
/*  656 */           Entry w = x.right;
/*  657 */           x.right = w.left;
/*  658 */           w.left = x;
/*  659 */           y.left = w.right;
/*  660 */           w.right = y;
/*  661 */           if (w.balance() == -1) {
/*  662 */             x.balance(0);
/*  663 */             y.balance(1);
/*  664 */           } else if (w.balance() == 0) {
/*  665 */             x.balance(0);
/*  666 */             y.balance(0);
/*      */           } else {
/*  668 */             assert w.balance() == 1;
/*  669 */             x.balance(-1);
/*  670 */             y.balance(0);
/*      */           } 
/*  672 */           w.balance(0);
/*  673 */           if (w.pred()) {
/*  674 */             x.succ(w);
/*  675 */             w.pred(false);
/*      */           } 
/*  677 */           if (w.succ()) {
/*  678 */             y.pred(w);
/*  679 */             w.succ(false);
/*      */           } 
/*  681 */           if (q != null) {
/*  682 */             if (dir) {
/*  683 */               q.right = w; continue;
/*      */             } 
/*  685 */             q.left = w; continue;
/*      */           } 
/*  687 */           this.tree = w; continue;
/*      */         } 
/*  689 */         if (q != null)
/*  690 */         { if (dir) {
/*  691 */             q.right = x;
/*      */           } else {
/*  693 */             q.left = x;
/*      */           }  }
/*  695 */         else { this.tree = x; }
/*  696 */          if (x.balance() == 0) {
/*  697 */           y.left = x.right;
/*  698 */           x.right = y;
/*  699 */           x.balance(1);
/*  700 */           y.balance(-1);
/*      */           break;
/*      */         } 
/*  703 */         assert x.balance() == -1;
/*  704 */         if (x.succ()) {
/*  705 */           y.pred(true);
/*  706 */           x.succ(false);
/*      */         } else {
/*  708 */           y.left = x.right;
/*  709 */         }  x.right = y;
/*  710 */         y.balance(0);
/*  711 */         x.balance(0);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  716 */     this.modified = true;
/*  717 */     this.count--;
/*  718 */     return p.value;
/*      */   }
/*      */   
/*      */   public boolean containsValue(int v) {
/*  722 */     ValueIterator i = new ValueIterator();
/*      */     
/*  724 */     int j = this.count;
/*  725 */     while (j-- != 0) {
/*  726 */       int ev = i.nextInt();
/*  727 */       if (ev == v)
/*  728 */         return true; 
/*      */     } 
/*  730 */     return false;
/*      */   }
/*      */   
/*      */   public void clear() {
/*  734 */     this.count = 0;
/*  735 */     this.tree = null;
/*  736 */     this.entries = null;
/*  737 */     this.values = null;
/*  738 */     this.keys = null;
/*  739 */     this.firstEntry = this.lastEntry = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Entry
/*      */     extends AbstractDouble2IntMap.BasicEntry
/*      */     implements Cloneable
/*      */   {
/*      */     private static final int SUCC_MASK = -2147483648;
/*      */ 
/*      */ 
/*      */     
/*      */     private static final int PRED_MASK = 1073741824;
/*      */ 
/*      */     
/*      */     private static final int BALANCE_MASK = 255;
/*      */ 
/*      */     
/*      */     Entry left;
/*      */ 
/*      */     
/*      */     Entry right;
/*      */ 
/*      */     
/*      */     int info;
/*      */ 
/*      */ 
/*      */     
/*      */     Entry() {
/*  770 */       super(0.0D, 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry(double k, int v) {
/*  781 */       super(k, v);
/*  782 */       this.info = -1073741824;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry left() {
/*  790 */       return ((this.info & 0x40000000) != 0) ? null : this.left;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry right() {
/*  798 */       return ((this.info & Integer.MIN_VALUE) != 0) ? null : this.right;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean pred() {
/*  806 */       return ((this.info & 0x40000000) != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean succ() {
/*  814 */       return ((this.info & Integer.MIN_VALUE) != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void pred(boolean pred) {
/*  823 */       if (pred) {
/*  824 */         this.info |= 0x40000000;
/*      */       } else {
/*  826 */         this.info &= 0xBFFFFFFF;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void succ(boolean succ) {
/*  835 */       if (succ) {
/*  836 */         this.info |= Integer.MIN_VALUE;
/*      */       } else {
/*  838 */         this.info &= Integer.MAX_VALUE;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void pred(Entry pred) {
/*  847 */       this.info |= 0x40000000;
/*  848 */       this.left = pred;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void succ(Entry succ) {
/*  857 */       this.info |= Integer.MIN_VALUE;
/*  858 */       this.right = succ;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void left(Entry left) {
/*  867 */       this.info &= 0xBFFFFFFF;
/*  868 */       this.left = left;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void right(Entry right) {
/*  877 */       this.info &= Integer.MAX_VALUE;
/*  878 */       this.right = right;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int balance() {
/*  886 */       return (byte)this.info;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void balance(int level) {
/*  895 */       this.info &= 0xFFFFFF00;
/*  896 */       this.info |= level & 0xFF;
/*      */     }
/*      */     
/*      */     void incBalance() {
/*  900 */       this.info = this.info & 0xFFFFFF00 | (byte)this.info + 1 & 0xFF;
/*      */     }
/*      */     
/*      */     protected void decBalance() {
/*  904 */       this.info = this.info & 0xFFFFFF00 | (byte)this.info - 1 & 0xFF;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry next() {
/*  912 */       Entry next = this.right;
/*  913 */       if ((this.info & Integer.MIN_VALUE) == 0)
/*  914 */         while ((next.info & 0x40000000) == 0)
/*  915 */           next = next.left;  
/*  916 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Entry prev() {
/*  924 */       Entry prev = this.left;
/*  925 */       if ((this.info & 0x40000000) == 0)
/*  926 */         while ((prev.info & Integer.MIN_VALUE) == 0)
/*  927 */           prev = prev.right;  
/*  928 */       return prev;
/*      */     }
/*      */     
/*      */     public int setValue(int value) {
/*  932 */       int oldValue = this.value;
/*  933 */       this.value = value;
/*  934 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public Entry clone() {
/*      */       Entry c;
/*      */       try {
/*  941 */         c = (Entry)super.clone();
/*  942 */       } catch (CloneNotSupportedException cantHappen) {
/*  943 */         throw new InternalError();
/*      */       } 
/*  945 */       c.key = this.key;
/*  946 */       c.value = this.value;
/*  947 */       c.info = this.info;
/*  948 */       return c;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  953 */       if (!(o instanceof Map.Entry))
/*  954 */         return false; 
/*  955 */       Map.Entry<Double, Integer> e = (Map.Entry<Double, Integer>)o;
/*  956 */       return (Double.doubleToLongBits(this.key) == Double.doubleToLongBits(((Double)e.getKey()).doubleValue()) && this.value == ((Integer)e
/*  957 */         .getValue()).intValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  961 */       return HashCommon.double2int(this.key) ^ this.value;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  965 */       return this.key + "=>" + this.value;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(double k) {
/*  986 */     return (findKey(k) != null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  990 */     return this.count;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  994 */     return (this.count == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public int get(double k) {
/*  999 */     Entry e = findKey(k);
/* 1000 */     return (e == null) ? this.defRetValue : e.value;
/*      */   }
/*      */   
/*      */   public double firstDoubleKey() {
/* 1004 */     if (this.tree == null)
/* 1005 */       throw new NoSuchElementException(); 
/* 1006 */     return this.firstEntry.key;
/*      */   }
/*      */   
/*      */   public double lastDoubleKey() {
/* 1010 */     if (this.tree == null)
/* 1011 */       throw new NoSuchElementException(); 
/* 1012 */     return this.lastEntry.key;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class TreeIterator
/*      */   {
/*      */     Double2IntAVLTreeMap.Entry prev;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Double2IntAVLTreeMap.Entry next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Double2IntAVLTreeMap.Entry curr;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1043 */     int index = 0;
/*      */     TreeIterator() {
/* 1045 */       this.next = Double2IntAVLTreeMap.this.firstEntry;
/*      */     }
/*      */     TreeIterator(double k) {
/* 1048 */       if ((this.next = Double2IntAVLTreeMap.this.locateKey(k)) != null)
/* 1049 */         if (Double2IntAVLTreeMap.this.compare(this.next.key, k) <= 0) {
/* 1050 */           this.prev = this.next;
/* 1051 */           this.next = this.next.next();
/*      */         } else {
/* 1053 */           this.prev = this.next.prev();
/*      */         }  
/*      */     }
/*      */     public boolean hasNext() {
/* 1057 */       return (this.next != null);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1060 */       return (this.prev != null);
/*      */     }
/*      */     void updateNext() {
/* 1063 */       this.next = this.next.next();
/*      */     }
/*      */     Double2IntAVLTreeMap.Entry nextEntry() {
/* 1066 */       if (!hasNext())
/* 1067 */         throw new NoSuchElementException(); 
/* 1068 */       this.curr = this.prev = this.next;
/* 1069 */       this.index++;
/* 1070 */       updateNext();
/* 1071 */       return this.curr;
/*      */     }
/*      */     void updatePrevious() {
/* 1074 */       this.prev = this.prev.prev();
/*      */     }
/*      */     Double2IntAVLTreeMap.Entry previousEntry() {
/* 1077 */       if (!hasPrevious())
/* 1078 */         throw new NoSuchElementException(); 
/* 1079 */       this.curr = this.next = this.prev;
/* 1080 */       this.index--;
/* 1081 */       updatePrevious();
/* 1082 */       return this.curr;
/*      */     }
/*      */     public int nextIndex() {
/* 1085 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1088 */       return this.index - 1;
/*      */     }
/*      */     public void remove() {
/* 1091 */       if (this.curr == null) {
/* 1092 */         throw new IllegalStateException();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1097 */       if (this.curr == this.prev)
/* 1098 */         this.index--; 
/* 1099 */       this.next = this.prev = this.curr;
/* 1100 */       updatePrevious();
/* 1101 */       updateNext();
/* 1102 */       Double2IntAVLTreeMap.this.remove(this.curr.key);
/* 1103 */       this.curr = null;
/*      */     }
/*      */     public int skip(int n) {
/* 1106 */       int i = n;
/* 1107 */       while (i-- != 0 && hasNext())
/* 1108 */         nextEntry(); 
/* 1109 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1112 */       int i = n;
/* 1113 */       while (i-- != 0 && hasPrevious())
/* 1114 */         previousEntry(); 
/* 1115 */       return n - i - 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class EntryIterator
/*      */     extends TreeIterator
/*      */     implements ObjectListIterator<Double2IntMap.Entry>
/*      */   {
/*      */     EntryIterator() {}
/*      */ 
/*      */     
/*      */     EntryIterator(double k) {
/* 1128 */       super(k);
/*      */     }
/*      */     
/*      */     public Double2IntMap.Entry next() {
/* 1132 */       return nextEntry();
/*      */     }
/*      */     
/*      */     public Double2IntMap.Entry previous() {
/* 1136 */       return previousEntry();
/*      */     }
/*      */     
/*      */     public void set(Double2IntMap.Entry ok) {
/* 1140 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void add(Double2IntMap.Entry ok) {
/* 1144 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   public ObjectSortedSet<Double2IntMap.Entry> double2IntEntrySet() {
/* 1149 */     if (this.entries == null)
/* 1150 */       this.entries = (ObjectSortedSet<Double2IntMap.Entry>)new AbstractObjectSortedSet<Double2IntMap.Entry>()
/*      */         {
/*      */           final Comparator<? super Double2IntMap.Entry> comparator;
/*      */           
/*      */           public Comparator<? super Double2IntMap.Entry> comparator() {
/* 1155 */             return this.comparator;
/*      */           }
/*      */           
/*      */           public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator() {
/* 1159 */             return (ObjectBidirectionalIterator<Double2IntMap.Entry>)new Double2IntAVLTreeMap.EntryIterator();
/*      */           }
/*      */           
/*      */           public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator(Double2IntMap.Entry from) {
/* 1163 */             return (ObjectBidirectionalIterator<Double2IntMap.Entry>)new Double2IntAVLTreeMap.EntryIterator(from.getDoubleKey());
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean contains(Object o) {
/* 1168 */             if (!(o instanceof Map.Entry))
/* 1169 */               return false; 
/* 1170 */             Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1171 */             if (e.getKey() == null || !(e.getKey() instanceof Double))
/* 1172 */               return false; 
/* 1173 */             if (e.getValue() == null || !(e.getValue() instanceof Integer))
/* 1174 */               return false; 
/* 1175 */             Double2IntAVLTreeMap.Entry f = Double2IntAVLTreeMap.this.findKey(((Double)e.getKey()).doubleValue());
/* 1176 */             return e.equals(f);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean remove(Object o) {
/* 1181 */             if (!(o instanceof Map.Entry))
/* 1182 */               return false; 
/* 1183 */             Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1184 */             if (e.getKey() == null || !(e.getKey() instanceof Double))
/* 1185 */               return false; 
/* 1186 */             if (e.getValue() == null || !(e.getValue() instanceof Integer))
/* 1187 */               return false; 
/* 1188 */             Double2IntAVLTreeMap.Entry f = Double2IntAVLTreeMap.this.findKey(((Double)e.getKey()).doubleValue());
/* 1189 */             if (f == null || f.getIntValue() != ((Integer)e.getValue()).intValue())
/* 1190 */               return false; 
/* 1191 */             Double2IntAVLTreeMap.this.remove(f.key);
/* 1192 */             return true;
/*      */           }
/*      */           
/*      */           public int size() {
/* 1196 */             return Double2IntAVLTreeMap.this.count;
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1200 */             Double2IntAVLTreeMap.this.clear();
/*      */           }
/*      */           
/*      */           public Double2IntMap.Entry first() {
/* 1204 */             return Double2IntAVLTreeMap.this.firstEntry;
/*      */           }
/*      */           
/*      */           public Double2IntMap.Entry last() {
/* 1208 */             return Double2IntAVLTreeMap.this.lastEntry;
/*      */           }
/*      */           
/*      */           public ObjectSortedSet<Double2IntMap.Entry> subSet(Double2IntMap.Entry from, Double2IntMap.Entry to) {
/* 1212 */             return Double2IntAVLTreeMap.this.subMap(from.getDoubleKey(), to.getDoubleKey()).double2IntEntrySet();
/*      */           }
/*      */           
/*      */           public ObjectSortedSet<Double2IntMap.Entry> headSet(Double2IntMap.Entry to) {
/* 1216 */             return Double2IntAVLTreeMap.this.headMap(to.getDoubleKey()).double2IntEntrySet();
/*      */           }
/*      */           
/*      */           public ObjectSortedSet<Double2IntMap.Entry> tailSet(Double2IntMap.Entry from) {
/* 1220 */             return Double2IntAVLTreeMap.this.tailMap(from.getDoubleKey()).double2IntEntrySet();
/*      */           }
/*      */         }; 
/* 1223 */     return this.entries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class KeyIterator
/*      */     extends TreeIterator
/*      */     implements DoubleListIterator
/*      */   {
/*      */     public KeyIterator() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public KeyIterator(double k) {
/* 1239 */       super(k);
/*      */     }
/*      */     
/*      */     public double nextDouble() {
/* 1243 */       return (nextEntry()).key;
/*      */     }
/*      */     
/*      */     public double previousDouble() {
/* 1247 */       return (previousEntry()).key;
/*      */     } }
/*      */   
/*      */   private class KeySet extends AbstractDouble2IntSortedMap.KeySet {
/*      */     private KeySet() {}
/*      */     
/*      */     public DoubleBidirectionalIterator iterator() {
/* 1254 */       return new Double2IntAVLTreeMap.KeyIterator();
/*      */     }
/*      */     
/*      */     public DoubleBidirectionalIterator iterator(double from) {
/* 1258 */       return new Double2IntAVLTreeMap.KeyIterator(from);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleSortedSet keySet() {
/* 1273 */     if (this.keys == null)
/* 1274 */       this.keys = new KeySet(); 
/* 1275 */     return this.keys;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class ValueIterator
/*      */     extends TreeIterator
/*      */     implements IntListIterator
/*      */   {
/*      */     private ValueIterator() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public int nextInt() {
/* 1290 */       return (nextEntry()).value;
/*      */     }
/*      */     
/*      */     public int previousInt() {
/* 1294 */       return (previousEntry()).value;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IntCollection values() {
/* 1309 */     if (this.values == null)
/* 1310 */       this.values = (IntCollection)new AbstractIntCollection()
/*      */         {
/*      */           public IntIterator iterator() {
/* 1313 */             return (IntIterator)new Double2IntAVLTreeMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public boolean contains(int k) {
/* 1317 */             return Double2IntAVLTreeMap.this.containsValue(k);
/*      */           }
/*      */           
/*      */           public int size() {
/* 1321 */             return Double2IntAVLTreeMap.this.count;
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1325 */             Double2IntAVLTreeMap.this.clear();
/*      */           }
/*      */         }; 
/* 1328 */     return this.values;
/*      */   }
/*      */   
/*      */   public DoubleComparator comparator() {
/* 1332 */     return this.actualComparator;
/*      */   }
/*      */   
/*      */   public Double2IntSortedMap headMap(double to) {
/* 1336 */     return new Submap(0.0D, true, to, false);
/*      */   }
/*      */   
/*      */   public Double2IntSortedMap tailMap(double from) {
/* 1340 */     return new Submap(from, false, 0.0D, true);
/*      */   }
/*      */   
/*      */   public Double2IntSortedMap subMap(double from, double to) {
/* 1344 */     return new Submap(from, false, to, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class Submap
/*      */     extends AbstractDouble2IntSortedMap
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -7046029254386353129L;
/*      */ 
/*      */ 
/*      */     
/*      */     double from;
/*      */ 
/*      */ 
/*      */     
/*      */     double to;
/*      */ 
/*      */ 
/*      */     
/*      */     boolean bottom;
/*      */ 
/*      */ 
/*      */     
/*      */     boolean top;
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient ObjectSortedSet<Double2IntMap.Entry> entries;
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient DoubleSortedSet keys;
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient IntCollection values;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Submap(double from, boolean bottom, double to, boolean top) {
/* 1388 */       if (!bottom && !top && Double2IntAVLTreeMap.this.compare(from, to) > 0)
/* 1389 */         throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")"); 
/* 1390 */       this.from = from;
/* 1391 */       this.bottom = bottom;
/* 1392 */       this.to = to;
/* 1393 */       this.top = top;
/* 1394 */       this.defRetValue = Double2IntAVLTreeMap.this.defRetValue;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1398 */       SubmapIterator i = new SubmapIterator();
/* 1399 */       while (i.hasNext()) {
/* 1400 */         i.nextEntry();
/* 1401 */         i.remove();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean in(double k) {
/* 1412 */       return ((this.bottom || Double2IntAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Double2IntAVLTreeMap.this
/* 1413 */         .compare(k, this.to) < 0));
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Double2IntMap.Entry> double2IntEntrySet() {
/* 1417 */       if (this.entries == null)
/* 1418 */         this.entries = (ObjectSortedSet<Double2IntMap.Entry>)new AbstractObjectSortedSet<Double2IntMap.Entry>()
/*      */           {
/*      */             public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator() {
/* 1421 */               return (ObjectBidirectionalIterator<Double2IntMap.Entry>)new Double2IntAVLTreeMap.Submap.SubmapEntryIterator();
/*      */             }
/*      */             
/*      */             public ObjectBidirectionalIterator<Double2IntMap.Entry> iterator(Double2IntMap.Entry from) {
/* 1425 */               return (ObjectBidirectionalIterator<Double2IntMap.Entry>)new Double2IntAVLTreeMap.Submap.SubmapEntryIterator(from.getDoubleKey());
/*      */             }
/*      */             
/*      */             public Comparator<? super Double2IntMap.Entry> comparator() {
/* 1429 */               return Double2IntAVLTreeMap.this.double2IntEntrySet().comparator();
/*      */             }
/*      */ 
/*      */             
/*      */             public boolean contains(Object o) {
/* 1434 */               if (!(o instanceof Map.Entry))
/* 1435 */                 return false; 
/* 1436 */               Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1437 */               if (e.getKey() == null || !(e.getKey() instanceof Double))
/* 1438 */                 return false; 
/* 1439 */               if (e.getValue() == null || !(e.getValue() instanceof Integer))
/* 1440 */                 return false; 
/* 1441 */               Double2IntAVLTreeMap.Entry f = Double2IntAVLTreeMap.this.findKey(((Double)e.getKey()).doubleValue());
/* 1442 */               return (f != null && Double2IntAVLTreeMap.Submap.this.in(f.key) && e.equals(f));
/*      */             }
/*      */ 
/*      */             
/*      */             public boolean remove(Object o) {
/* 1447 */               if (!(o instanceof Map.Entry))
/* 1448 */                 return false; 
/* 1449 */               Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1450 */               if (e.getKey() == null || !(e.getKey() instanceof Double))
/* 1451 */                 return false; 
/* 1452 */               if (e.getValue() == null || !(e.getValue() instanceof Integer))
/* 1453 */                 return false; 
/* 1454 */               Double2IntAVLTreeMap.Entry f = Double2IntAVLTreeMap.this.findKey(((Double)e.getKey()).doubleValue());
/* 1455 */               if (f != null && Double2IntAVLTreeMap.Submap.this.in(f.key))
/* 1456 */                 Double2IntAVLTreeMap.Submap.this.remove(f.key); 
/* 1457 */               return (f != null);
/*      */             }
/*      */             
/*      */             public int size() {
/* 1461 */               int c = 0;
/* 1462 */               for (ObjectBidirectionalIterator<Double2IntMap.Entry> objectBidirectionalIterator = iterator(); objectBidirectionalIterator.hasNext(); objectBidirectionalIterator.next())
/* 1463 */                 c++; 
/* 1464 */               return c;
/*      */             }
/*      */             
/*      */             public boolean isEmpty() {
/* 1468 */               return !(new Double2IntAVLTreeMap.Submap.SubmapIterator()).hasNext();
/*      */             }
/*      */             
/*      */             public void clear() {
/* 1472 */               Double2IntAVLTreeMap.Submap.this.clear();
/*      */             }
/*      */             
/*      */             public Double2IntMap.Entry first() {
/* 1476 */               return Double2IntAVLTreeMap.Submap.this.firstEntry();
/*      */             }
/*      */             
/*      */             public Double2IntMap.Entry last() {
/* 1480 */               return Double2IntAVLTreeMap.Submap.this.lastEntry();
/*      */             }
/*      */ 
/*      */             
/*      */             public ObjectSortedSet<Double2IntMap.Entry> subSet(Double2IntMap.Entry from, Double2IntMap.Entry to) {
/* 1485 */               return Double2IntAVLTreeMap.Submap.this.subMap(from.getDoubleKey(), to.getDoubleKey()).double2IntEntrySet();
/*      */             }
/*      */             
/*      */             public ObjectSortedSet<Double2IntMap.Entry> headSet(Double2IntMap.Entry to) {
/* 1489 */               return Double2IntAVLTreeMap.Submap.this.headMap(to.getDoubleKey()).double2IntEntrySet();
/*      */             }
/*      */             
/*      */             public ObjectSortedSet<Double2IntMap.Entry> tailSet(Double2IntMap.Entry from) {
/* 1493 */               return Double2IntAVLTreeMap.Submap.this.tailMap(from.getDoubleKey()).double2IntEntrySet();
/*      */             }
/*      */           }; 
/* 1496 */       return this.entries;
/*      */     }
/*      */     
/*      */     private class KeySet extends AbstractDouble2IntSortedMap.KeySet {
/*      */       public DoubleBidirectionalIterator iterator() {
/* 1501 */         return new Double2IntAVLTreeMap.Submap.SubmapKeyIterator();
/*      */       }
/*      */       private KeySet() {}
/*      */       public DoubleBidirectionalIterator iterator(double from) {
/* 1505 */         return new Double2IntAVLTreeMap.Submap.SubmapKeyIterator(from);
/*      */       }
/*      */     }
/*      */     
/*      */     public DoubleSortedSet keySet() {
/* 1510 */       if (this.keys == null)
/* 1511 */         this.keys = new KeySet(); 
/* 1512 */       return this.keys;
/*      */     }
/*      */     
/*      */     public IntCollection values() {
/* 1516 */       if (this.values == null)
/* 1517 */         this.values = (IntCollection)new AbstractIntCollection()
/*      */           {
/*      */             public IntIterator iterator() {
/* 1520 */               return (IntIterator)new Double2IntAVLTreeMap.Submap.SubmapValueIterator();
/*      */             }
/*      */             
/*      */             public boolean contains(int k) {
/* 1524 */               return Double2IntAVLTreeMap.Submap.this.containsValue(k);
/*      */             }
/*      */             
/*      */             public int size() {
/* 1528 */               return Double2IntAVLTreeMap.Submap.this.size();
/*      */             }
/*      */             
/*      */             public void clear() {
/* 1532 */               Double2IntAVLTreeMap.Submap.this.clear();
/*      */             }
/*      */           }; 
/* 1535 */       return this.values;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(double k) {
/* 1540 */       return (in(k) && Double2IntAVLTreeMap.this.containsKey(k));
/*      */     }
/*      */     
/*      */     public boolean containsValue(int v) {
/* 1544 */       SubmapIterator i = new SubmapIterator();
/*      */       
/* 1546 */       while (i.hasNext()) {
/* 1547 */         int ev = (i.nextEntry()).value;
/* 1548 */         if (ev == v)
/* 1549 */           return true; 
/*      */       } 
/* 1551 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int get(double k) {
/* 1557 */       double kk = k; Double2IntAVLTreeMap.Entry e;
/* 1558 */       return (in(kk) && (e = Double2IntAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
/*      */     }
/*      */     
/*      */     public int put(double k, int v) {
/* 1562 */       Double2IntAVLTreeMap.this.modified = false;
/* 1563 */       if (!in(k))
/* 1564 */         throw new IllegalArgumentException("Key (" + k + ") out of range [" + (
/* 1565 */             this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")"); 
/* 1566 */       int oldValue = Double2IntAVLTreeMap.this.put(k, v);
/* 1567 */       return Double2IntAVLTreeMap.this.modified ? this.defRetValue : oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(double k) {
/* 1572 */       Double2IntAVLTreeMap.this.modified = false;
/* 1573 */       if (!in(k))
/* 1574 */         return this.defRetValue; 
/* 1575 */       int oldValue = Double2IntAVLTreeMap.this.remove(k);
/* 1576 */       return Double2IntAVLTreeMap.this.modified ? oldValue : this.defRetValue;
/*      */     }
/*      */     
/*      */     public int size() {
/* 1580 */       SubmapIterator i = new SubmapIterator();
/* 1581 */       int n = 0;
/* 1582 */       while (i.hasNext()) {
/* 1583 */         n++;
/* 1584 */         i.nextEntry();
/*      */       } 
/* 1586 */       return n;
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1590 */       return !(new SubmapIterator()).hasNext();
/*      */     }
/*      */     
/*      */     public DoubleComparator comparator() {
/* 1594 */       return Double2IntAVLTreeMap.this.actualComparator;
/*      */     }
/*      */     
/*      */     public Double2IntSortedMap headMap(double to) {
/* 1598 */       if (this.top)
/* 1599 */         return new Submap(this.from, this.bottom, to, false); 
/* 1600 */       return (Double2IntAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
/*      */     }
/*      */     
/*      */     public Double2IntSortedMap tailMap(double from) {
/* 1604 */       if (this.bottom)
/* 1605 */         return new Submap(from, false, this.to, this.top); 
/* 1606 */       return (Double2IntAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
/*      */     }
/*      */     
/*      */     public Double2IntSortedMap subMap(double from, double to) {
/* 1610 */       if (this.top && this.bottom)
/* 1611 */         return new Submap(from, false, to, false); 
/* 1612 */       if (!this.top)
/* 1613 */         to = (Double2IntAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to; 
/* 1614 */       if (!this.bottom)
/* 1615 */         from = (Double2IntAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from; 
/* 1616 */       if (!this.top && !this.bottom && from == this.from && to == this.to)
/* 1617 */         return this; 
/* 1618 */       return new Submap(from, false, to, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Double2IntAVLTreeMap.Entry firstEntry() {
/*      */       Double2IntAVLTreeMap.Entry e;
/* 1627 */       if (Double2IntAVLTreeMap.this.tree == null) {
/* 1628 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1632 */       if (this.bottom) {
/* 1633 */         e = Double2IntAVLTreeMap.this.firstEntry;
/*      */       } else {
/* 1635 */         e = Double2IntAVLTreeMap.this.locateKey(this.from);
/*      */         
/* 1637 */         if (Double2IntAVLTreeMap.this.compare(e.key, this.from) < 0) {
/* 1638 */           e = e.next();
/*      */         }
/*      */       } 
/*      */       
/* 1642 */       if (e == null || (!this.top && Double2IntAVLTreeMap.this.compare(e.key, this.to) >= 0))
/* 1643 */         return null; 
/* 1644 */       return e;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Double2IntAVLTreeMap.Entry lastEntry() {
/*      */       Double2IntAVLTreeMap.Entry e;
/* 1653 */       if (Double2IntAVLTreeMap.this.tree == null) {
/* 1654 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1658 */       if (this.top) {
/* 1659 */         e = Double2IntAVLTreeMap.this.lastEntry;
/*      */       } else {
/* 1661 */         e = Double2IntAVLTreeMap.this.locateKey(this.to);
/*      */         
/* 1663 */         if (Double2IntAVLTreeMap.this.compare(e.key, this.to) >= 0) {
/* 1664 */           e = e.prev();
/*      */         }
/*      */       } 
/*      */       
/* 1668 */       if (e == null || (!this.bottom && Double2IntAVLTreeMap.this.compare(e.key, this.from) < 0))
/* 1669 */         return null; 
/* 1670 */       return e;
/*      */     }
/*      */     
/*      */     public double firstDoubleKey() {
/* 1674 */       Double2IntAVLTreeMap.Entry e = firstEntry();
/* 1675 */       if (e == null)
/* 1676 */         throw new NoSuchElementException(); 
/* 1677 */       return e.key;
/*      */     }
/*      */     
/*      */     public double lastDoubleKey() {
/* 1681 */       Double2IntAVLTreeMap.Entry e = lastEntry();
/* 1682 */       if (e == null)
/* 1683 */         throw new NoSuchElementException(); 
/* 1684 */       return e.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private class SubmapIterator
/*      */       extends Double2IntAVLTreeMap.TreeIterator
/*      */     {
/*      */       SubmapIterator() {
/* 1697 */         this.next = Double2IntAVLTreeMap.Submap.this.firstEntry();
/*      */       }
/*      */       SubmapIterator(double k) {
/* 1700 */         this();
/* 1701 */         if (this.next != null)
/* 1702 */           if (!Double2IntAVLTreeMap.Submap.this.bottom && Double2IntAVLTreeMap.this.compare(k, this.next.key) < 0) {
/* 1703 */             this.prev = null;
/* 1704 */           } else if (!Double2IntAVLTreeMap.Submap.this.top && Double2IntAVLTreeMap.this.compare(k, (this.prev = Double2IntAVLTreeMap.Submap.this.lastEntry()).key) >= 0) {
/* 1705 */             this.next = null;
/*      */           } else {
/* 1707 */             this.next = Double2IntAVLTreeMap.this.locateKey(k);
/* 1708 */             if (Double2IntAVLTreeMap.this.compare(this.next.key, k) <= 0) {
/* 1709 */               this.prev = this.next;
/* 1710 */               this.next = this.next.next();
/*      */             } else {
/* 1712 */               this.prev = this.next.prev();
/*      */             } 
/*      */           }  
/*      */       }
/*      */       
/*      */       void updatePrevious() {
/* 1718 */         this.prev = this.prev.prev();
/* 1719 */         if (!Double2IntAVLTreeMap.Submap.this.bottom && this.prev != null && Double2IntAVLTreeMap.this.compare(this.prev.key, Double2IntAVLTreeMap.Submap.this.from) < 0)
/* 1720 */           this.prev = null; 
/*      */       }
/*      */       
/*      */       void updateNext() {
/* 1724 */         this.next = this.next.next();
/* 1725 */         if (!Double2IntAVLTreeMap.Submap.this.top && this.next != null && Double2IntAVLTreeMap.this.compare(this.next.key, Double2IntAVLTreeMap.Submap.this.to) >= 0)
/* 1726 */           this.next = null; 
/*      */       } }
/*      */     
/*      */     private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Double2IntMap.Entry> {
/*      */       SubmapEntryIterator() {}
/*      */       
/*      */       SubmapEntryIterator(double k) {
/* 1733 */         super(k);
/*      */       }
/*      */       
/*      */       public Double2IntMap.Entry next() {
/* 1737 */         return nextEntry();
/*      */       }
/*      */       
/*      */       public Double2IntMap.Entry previous() {
/* 1741 */         return previousEntry();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final class SubmapKeyIterator
/*      */       extends SubmapIterator
/*      */       implements DoubleListIterator
/*      */     {
/*      */       public SubmapKeyIterator() {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public SubmapKeyIterator(double from) {
/* 1759 */         super(from);
/*      */       }
/*      */       
/*      */       public double nextDouble() {
/* 1763 */         return (nextEntry()).key;
/*      */       }
/*      */       
/*      */       public double previousDouble() {
/* 1767 */         return (previousEntry()).key;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final class SubmapValueIterator
/*      */       extends SubmapIterator
/*      */       implements IntListIterator
/*      */     {
/*      */       private SubmapValueIterator() {}
/*      */ 
/*      */ 
/*      */       
/*      */       public int nextInt() {
/* 1783 */         return (nextEntry()).value;
/*      */       }
/*      */       
/*      */       public int previousInt() {
/* 1787 */         return (previousEntry()).value;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2IntAVLTreeMap clone() {
/*      */     Double2IntAVLTreeMap c;
/*      */     try {
/* 1806 */       c = (Double2IntAVLTreeMap)super.clone();
/* 1807 */     } catch (CloneNotSupportedException cantHappen) {
/* 1808 */       throw new InternalError();
/*      */     } 
/* 1810 */     c.keys = null;
/* 1811 */     c.values = null;
/* 1812 */     c.entries = null;
/* 1813 */     c.allocatePaths();
/* 1814 */     if (this.count != 0) {
/*      */       
/* 1816 */       Entry rp = new Entry(), rq = new Entry();
/* 1817 */       Entry p = rp;
/* 1818 */       rp.left(this.tree);
/* 1819 */       Entry q = rq;
/* 1820 */       rq.pred((Entry)null);
/*      */       while (true) {
/* 1822 */         if (!p.pred()) {
/* 1823 */           Entry e = p.left.clone();
/* 1824 */           e.pred(q.left);
/* 1825 */           e.succ(q);
/* 1826 */           q.left(e);
/* 1827 */           p = p.left;
/* 1828 */           q = q.left;
/*      */         } else {
/* 1830 */           while (p.succ()) {
/* 1831 */             p = p.right;
/* 1832 */             if (p == null) {
/* 1833 */               q.right = null;
/* 1834 */               c.tree = rq.left;
/* 1835 */               c.firstEntry = c.tree;
/* 1836 */               while (c.firstEntry.left != null)
/* 1837 */                 c.firstEntry = c.firstEntry.left; 
/* 1838 */               c.lastEntry = c.tree;
/* 1839 */               while (c.lastEntry.right != null)
/* 1840 */                 c.lastEntry = c.lastEntry.right; 
/* 1841 */               return c;
/*      */             } 
/* 1843 */             q = q.right;
/*      */           } 
/* 1845 */           p = p.right;
/* 1846 */           q = q.right;
/*      */         } 
/* 1848 */         if (!p.succ()) {
/* 1849 */           Entry e = p.right.clone();
/* 1850 */           e.succ(q.right);
/* 1851 */           e.pred(q);
/* 1852 */           q.right(e);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1856 */     return c;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1859 */     int n = this.count;
/* 1860 */     EntryIterator i = new EntryIterator();
/*      */     
/* 1862 */     s.defaultWriteObject();
/* 1863 */     while (n-- != 0) {
/* 1864 */       Entry e = i.nextEntry();
/* 1865 */       s.writeDouble(e.key);
/* 1866 */       s.writeInt(e.value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Entry readTree(ObjectInputStream s, int n, Entry pred, Entry succ) throws IOException, ClassNotFoundException {
/* 1887 */     if (n == 1) {
/* 1888 */       Entry entry = new Entry(s.readDouble(), s.readInt());
/* 1889 */       entry.pred(pred);
/* 1890 */       entry.succ(succ);
/* 1891 */       return entry;
/*      */     } 
/* 1893 */     if (n == 2) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1898 */       Entry entry = new Entry(s.readDouble(), s.readInt());
/* 1899 */       entry.right(new Entry(s.readDouble(), s.readInt()));
/* 1900 */       entry.right.pred(entry);
/* 1901 */       entry.balance(1);
/* 1902 */       entry.pred(pred);
/* 1903 */       entry.right.succ(succ);
/* 1904 */       return entry;
/*      */     } 
/*      */     
/* 1907 */     int rightN = n / 2, leftN = n - rightN - 1;
/* 1908 */     Entry top = new Entry();
/* 1909 */     top.left(readTree(s, leftN, pred, top));
/* 1910 */     top.key = s.readDouble();
/* 1911 */     top.value = s.readInt();
/* 1912 */     top.right(readTree(s, rightN, top, succ));
/* 1913 */     if (n == (n & -n))
/* 1914 */       top.balance(1); 
/* 1915 */     return top;
/*      */   }
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1918 */     s.defaultReadObject();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1923 */     setActualComparator();
/* 1924 */     allocatePaths();
/* 1925 */     if (this.count != 0) {
/* 1926 */       this.tree = readTree(s, this.count, (Entry)null, (Entry)null);
/*      */       
/* 1928 */       Entry e = this.tree;
/* 1929 */       while (e.left() != null)
/* 1930 */         e = e.left(); 
/* 1931 */       this.firstEntry = e;
/* 1932 */       e = this.tree;
/* 1933 */       while (e.right() != null)
/* 1934 */         e = e.right(); 
/* 1935 */       this.lastEntry = e;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Syz\Downloads\Senura (1).jar!\i\\unimi\dsi\fastutil\doubles\Double2IntAVLTreeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
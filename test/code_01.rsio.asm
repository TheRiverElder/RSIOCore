  push.4 10
  save.4 12
  pop.4
  push.4 0
  save.4 8
  pop.4
L0:
  load.4 12
  push.4 0
  gt.4
  izj L1
  load.4 8
  load.4 12
  plus.4
  save.4 8
  pop.4
  load.4 12
  push.4 1
  sub.4
  save.4 12
  pop.4
  jmp L0
L1:
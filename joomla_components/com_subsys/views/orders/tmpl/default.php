<?php defined('_JEXEC') or die('Restricted access'); 
JHTML::_('behavior.tooltip');
?>
<form action="index.php" method="post" name="adminForm">
<div id="editcell">
<table>
<tr>
	  <td align="left">
			<?php 
				$options = array();
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A SUBSCRIBER'));
    //dump($this->subscribers, "SUBSCRIBERS");
    foreach($this->subscribers as $key=>$value) :
      ## Create $value ##
      #dump($value, "CATS");
      $options[] = JHTML::_('select.option', $value->sub_code, JText::_($value->sub_name).'('.$value->sub_code.')');
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'sub_code', '', 'value', 'text', $this->lists['subcode']);
				?>
			</td>
			<td align="left">
				<?php 
				$optionsFreq = array(JHTML::_('select.option','',JText::_('PLS CHOOSE TIMEFRAME(SUBS EXPIRY)')),
				  JHTML::_('select.option', 7 , JText::_('next 1 week')),
				  JHTML::_('select.option', 14, JText::_('next 2 weeks')),
				  JHTML::_('select.option', 30, JText::_('next 1 month')),
				  JHTML::_('select.option', 60, JText::_('next 2 months')),
				  JHTML::_('select.option', 180, JText::_('next 6 months'))
				);
    echo JHTML::_('select.genericlist', $optionsFreq, 'time_period', '', 'value', 'text', $this->lists['timeperiod']);
				?>
			</td>
				
				<td align="left">
				<button onclick="this.form.submit();"><?php echo JText::_( 'Go' ); ?></button>
				<button onclick="document.getElementById('sub_code').value='';document.getElementById('time_period').value='';this.form.submit();"><?php echo JText::_( 'Reset' ); ?></button>
			</td>
			
	</tr>
</table>
<?php if(!empty($this->items)) { ?>
<table width = '100%'>
  <tr><td align = 'left'>
  <?php 
  if(empty($this->lists['timeperiod']) && !empty($this->lists['subcode'])){
  echo JText::_( 'Unique orders: ' );  
  echo $this->summary; 
  echo JText::_( '(subscriptions: ' );  
  echo $this->lists['subcount'].')';
    }
  ?>
  </td></tr>
</table>
	<table class="adminlist">
	<thead>
		<tr>
			<th width="5">
				<?php echo JText::_( 'Database ID' ); ?>
			</th>
			<th width="20">
				<input type="checkbox" name="toggle" value="" onclick="checkAll(<?php echo count( $this->items ); ?>);" />
			</th>			
			<th>
			<?php echo JHTML::_('grid.sort', JText::_('Order Code'),'order_code', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
			<?php echo JHTML::_('grid.sort', JText::_('Order Date'),'order_date', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
			<?php echo JHTML::_('grid.sort', JText::_('Subscriber'),'sub_name', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Order Title' ); ?>
			</th>
			<th>
				<?php echo JHTML::_('grid.sort', JText::_('Invoice Number'),'order_invno', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JHTML::_('grid.sort', JText::_('Invoice Amount'),'order_invamt', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>			
				<?php echo JText::_( 'Amount Paid?' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Pub Code' ); ?>
			</th>
			<th>
				<?php echo JHTML::_('grid.sort', JText::_('Pub Name'),'pub_name', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>	
			<th>
				<?php echo JText::_( 'Sub Title' ); ?>
			</th>	
			<th>
			 <?php echo JHTML::_('grid.sort', JText::_('Num Issues'),'num_issues', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JHTML::_('grid.sort', JText::_('Copies'),'num_copies', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue From' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue To' ); ?>
			</th>
			<th>
				<?php echo JHTML::_('grid.sort', JText::_('StartDate'),'start_date', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JHTML::_('grid.sort', JText::_('EndDate'),'exp_date', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Created(Modified Date)' ); ?>
			</th>
		</tr>
	</thead>
	<?php
	$k = 0;
	for ($i=0, $n=count( $this->items ); $i < $n; $i++)	{
		$row =& $this->items[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->order_id );
		$link 		= JRoute::_( 'index.php?option=com_subsys&controller=order&task=edit&cid[]='. $row->order_id);		
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->order_id; ?>
			</td>
			<td>
				<?php echo $checked; ?>
			</td>
			<td>
				<?php echo $row->order_code; ?>
			</td>
			<td>
				<?php echo $row->order_date; ?>
			</td>
				<td>
				<?php echo $row->sub_name; ?>
			</td>
			<td>
				<?php echo $row->order_title; ?>
			</td>				
			<td>
				<?php echo $row->order_invno; ?>
			</td>
			<td>
				<?php echo $row->order_invamt; ?>
			</td>
			<td>
				<?php echo $row->order_paid; ?>
			</td>
			<td>
				<?php echo $row->pub_code; ?>
			</td>
			<td>
				<?php echo $row->pub_name; ?>
			</td>
			<td>
				<?php echo $row->subscription_title; ?>
			</td>						
			<td>
				<?php echo $row->num_issues; ?>
			</td>
			<td>
				<?php echo $row->num_copies; ?>
			</td>
				<td>
				<?php echo $row->issue_from; ?>
			</td>
			<td>
				<?php echo $row->issue_to; ?>
			</td>	
			<td>
				<?php echo $row->start_date; ?>
			</td>
			<td>
				<?php echo $row->exp_date; ?>
			</td>
			<td>
				<?php echo $row->cdate.'('.$row->mdate.')'; ?>
			</td>			
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>
	<tfoot>
    <tr>
      <td colspan="19"><?php echo $this->pagination->getListFooter(); ?></td>
    </tr>
  </tfoot>
	</table>
	<?php } ?>
</div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="boxchecked" value="0" />
<input type="hidden" name="controller" value="order" />
<input type="hidden" name="filter_order" value="<?php echo $this->lists['order']; ?>" />
<input type="hidden" name="filter_order_Dir" value="<?php echo $this->lists['order_Dir']; ?>" />
</form>
